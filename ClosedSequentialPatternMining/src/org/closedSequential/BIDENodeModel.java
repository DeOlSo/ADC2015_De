package org.closedSequential;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.StringValue;
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import spmf.bide.*;


/**
 * This is the model implementation of BIDE.
 * Implementation of the BIDE Algorithm for mining closed frequent sequential patterns
 *
 * @author Dennis S
 */
public class BIDENodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(BIDENodeModel.class);
        
    /** the settings key which is used to retrieve and 
        store the settings (from the dialog or from a settings file)    
       (package visibility to be usable from the dialog). */

	static final String CFGKEY_MINSUP = "MinSup";
	static final String CFGKEY_SHOWSEQUENCE = "ShowSequence";
	static final String CFGKEY_COLSEL = "ColSel";
	

    /** initial default values. */	
    static final double DEFAULT_MINSUP = 0.01;
    static final boolean DEFAULT_SHOWSEQUENCE = false;
    static final String DEFAULT_COLSEL = "";



    
  
    
    private final SettingsModelDoubleBounded m_minsup =
            new SettingsModelDoubleBounded(
                BIDENodeModel.CFGKEY_MINSUP,
                BIDENodeModel.DEFAULT_MINSUP,
                0.01, 1);
    
    
   private final SettingsModelBoolean m_showsequence =
    		new SettingsModelBoolean(
    				BIDENodeModel.CFGKEY_SHOWSEQUENCE,
    				BIDENodeModel.DEFAULT_SHOWSEQUENCE);
    

   
   private final SettingsModelColumnName m_colsel =
   		new SettingsModelColumnName(
   				BIDENodeModel.CFGKEY_COLSEL,
   				BIDENodeModel.DEFAULT_COLSEL);  
   
   
   
   private AlgoBIDEPlus algoBide;
   
   
    /**
     * Constructor for the node model.
     */
    protected BIDENodeModel() {
    
        // TODO one incoming port and one outgoing port is assumed
        super(1, 1);
    }

    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

        
        
        algoBide = new AlgoBIDEPlus();
        algoBide.setShowSequenceIdentifiers(m_showsequence.getBooleanValue());
        SequenceDatabase db = new SequenceDatabase();
        db.loadTable(inData, m_colsel.getColumnName());
        int sup = (int) (inData[0].getRowCount() * m_minsup.getDoubleValue());
        ArrayList<String[]>  outputData = algoBide.runAlgorithm(db, sup);
        
        

        

        
     //   ArrayList<String[]> test = algo.runAlgorithm(inData, m_colsel.getColumnName(), m_minsup.getDoubleValue() , m_showsequence.getBooleanValue());
     //   ArrayList<String[]> test = new ArrayList<String[]>();
   //     test.add(new String[]{"1","2","3.0","4"});

        
        // the data table spec of the single output table, 
        // the table will have three columns:
        DataColumnSpec[] allColSpecs = new DataColumnSpec[4];
        allColSpecs[0] = 
            new DataColumnSpecCreator("Pattern", StringCell.TYPE).createSpec();
        allColSpecs[1] = 
            new DataColumnSpecCreator("Support", IntCell.TYPE).createSpec();
        allColSpecs[2] = 
            new DataColumnSpecCreator("Support in %", DoubleCell.TYPE).createSpec();
        allColSpecs[3] =
        	new DataColumnSpecCreator("Sequence ids", StringCell.TYPE).createSpec();
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        // the execution context will provide us with storage capacity, in this
        // case a data container to which we will add rows sequentially
        // Note, this container can also handle arbitrary big data tables, it
        // will buffer to disc if necessary.
        BufferedDataContainer container = exec.createDataContainer(outputSpec);

        int rowCount = inData[0].getRowCount();
        for(int k = 0; k < outputData.size(); k++){
        RowKey key = new RowKey("Row " + k);
        DataCell[] cells = new DataCell[4];
        cells[0] = new StringCell(outputData.get(k)[0] + "-2"); 
        int support = Integer.parseInt(outputData.get(k)[1].replaceAll("[^\\d]", ""));
        cells[1] = new IntCell(support); 
        cells[2] = new DoubleCell((support*100)/(double)rowCount);
        if(m_showsequence.getBooleanValue() == true){
        	cells[3] = new StringCell(outputData.get(k)[2]);
        } else {
        	cells[3] = new StringCell("");
        }
        
        DataRow row = new DefaultRow(key, cells);
        container.addRowToTable(row);
        
        // check if the execution monitor was canceled
        exec.checkCanceled();
        exec.setProgress(k / (double)outputData.size(), 
            "Adding row " + k + " of " + outputData.size());
        }
        

        // once we are done, we close the container and return its table
        container.close();
        BufferedDataTable out = container.getTable();
        
        return new BufferedDataTable[]{out};
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // TODO Code executed on reset.
        // Models build during execute are cleared here.
        // Also data handled in load/saveInternals will be erased here.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
        
        // TODO: check if user settings are available, fit to the incoming
        // table structure, and the incoming types are feasible for the node
        // to execute. If the node can execute in its current state return
        // the spec of its output data table(s) (if you can, otherwise an array
        // with null elements), or throw an exception with a useful user message

        return new DataTableSpec[]{null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {

        // TODO save user settings to the config object.
        
        m_minsup.saveSettingsTo(settings);
        m_showsequence.saveSettingsTo(settings);
        m_colsel.saveSettingsTo(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        // TODO load (valid) settings from the config object.
        // It can be safely assumed that the settings are valided by the 
        // method below.
        
        m_minsup.loadSettingsFrom(settings);
        m_showsequence.loadSettingsFrom(settings);
        m_colsel.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        // TODO check if the settings could be applied to our model
        // e.g. if the count is in a certain range (which is ensured by the
        // SettingsModel).
        // Do not actually set any values of any member variables.

        m_minsup.validateSettings(settings);
        m_showsequence.validateSettings(settings);
        m_colsel.validateSettings(settings);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        
        // TODO load internal data. 
        // Everything handed to output ports is loaded automatically (data
        // returned by the execute method, models loaded in loadModelContent,
        // and user settings set through loadSettingsFrom - is all taken care 
        // of). Load here only the other internals that need to be restored
        // (e.g. data used by the views).

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
       
        // TODO save internal models. 
        // Everything written to output ports is saved automatically (data
        // returned by the execute method, models saved in the saveModelContent,
        // and user settings saved through saveSettingsTo - is all taken care 
        // of). Save here only the other internals that need to be preserved
        // (e.g. data used by the views).

    }
    
    
    
    public AlgoBIDEPlus getAlgo(){
    	return algoBide;
    }

}

