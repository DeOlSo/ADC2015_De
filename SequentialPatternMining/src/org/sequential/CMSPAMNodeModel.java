package org.sequential;

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

import spmf.spam.AlgoCMSPAM;


/**
 * This is the model implementation of CMSPAM.
 * Implementation of the CM-SPAM Algorithm for mining frequent sequential patterns
 *
 * @author Dennis S
 */
public class CMSPAMNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(CMSPAMNodeModel.class);
        
    /** the settings key which is used to retrieve and 
        store the settings (from the dialog or from a settings file)    
       (package visibility to be usable from the dialog). */

	static final String CFGKEY_MINSUP = "MinSup";
	static final String CFGKEY_MINPATTERN = "MinPattern";
	static final String CFGKEY_MAXPATTERN = "MaxPattern";
	static final String CFGKEY_REQUIREDITEMS = "RequiredItems";
	static final String CFGKEY_MAXGAP = "MaxGap";
	static final String CFGKEY_SHOWSEQUENCE = "ShowSequence";
	static final String CFGKEY_COLSEL = "ColSel";
	

    /** initial default values. */	
    static final double DEFAULT_MINSUP = 0.01;
    static final int DEFAULT_MINPATTERN = 1;
    static final int DEFAULT_MAXPATTERN = 0;
    static final String DEFAULT_REQUIREDITEMS = "";
    static final int DEFAULT_MAXGAP = 0;
    static final boolean DEFAULT_SHOWSEQUENCE = false;
    static final String DEFAULT_COLSEL = "";



    
  
    
    private final SettingsModelDoubleBounded m_minsup =
            new SettingsModelDoubleBounded(
                CMSPAMNodeModel.CFGKEY_MINSUP,
                CMSPAMNodeModel.DEFAULT_MINSUP,
                0.01, 1);
    
    
    private final SettingsModelIntegerBounded m_minpattern =
            new SettingsModelIntegerBounded(
                CMSPAMNodeModel.CFGKEY_MINPATTERN,
                CMSPAMNodeModel.DEFAULT_MINPATTERN,
                1, Integer.MAX_VALUE);
    
    private final SettingsModelIntegerBounded m_maxpattern =
            new SettingsModelIntegerBounded(
                CMSPAMNodeModel.CFGKEY_MAXPATTERN,
                CMSPAMNodeModel.DEFAULT_MAXPATTERN,
                0, Integer.MAX_VALUE);
    
    
    private final SettingsModelString m_requireditems =
    		new SettingsModelString(
    				CMSPAMNodeModel.CFGKEY_REQUIREDITEMS,
    				CMSPAMNodeModel.DEFAULT_REQUIREDITEMS);
    
    
    private final SettingsModelIntegerBounded m_maxgap =
            new SettingsModelIntegerBounded(
                CMSPAMNodeModel.CFGKEY_MAXGAP,
                CMSPAMNodeModel.DEFAULT_MAXGAP,
                0, Integer.MAX_VALUE);
    
   private final SettingsModelBoolean m_showsequence =
    		new SettingsModelBoolean(
    				CMSPAMNodeModel.CFGKEY_SHOWSEQUENCE,
    				CMSPAMNodeModel.DEFAULT_SHOWSEQUENCE);
    

   
   private final SettingsModelColumnName m_colsel =
   		new SettingsModelColumnName(
   				CMSPAMNodeModel.CFGKEY_COLSEL,
   				CMSPAMNodeModel.DEFAULT_COLSEL);  
   
   
   
   private AlgoCMSPAM algo;
   
    /**
     * Constructor for the node model.
     */
    protected CMSPAMNodeModel() {
    
        // TODO one incoming port and one outgoing port is assumed
        super(1, 1);
    }

    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

        algo = new AlgoCMSPAM();
        if (0 < m_maxgap.getIntValue()){
        	algo.setMaxGap(m_maxgap.getIntValue());
        }
        if (0 < m_maxpattern.getIntValue()){
        	algo.setMaximumPatternLength(m_maxpattern.getIntValue());
        }
        algo.setMinimumPatternLength(m_minpattern.getIntValue());
        
        
        
        //creating int array out of String
        String requiredString = m_requireditems.getStringValue();
        if(0 < requiredString.length()){
        requiredString = requiredString.replaceAll("[^\\d,]", "");
        String[] requiredArray =  requiredString.split(",");
        int empty = 0;
        for(String s : requiredArray){
        	if(s.isEmpty()){
        		empty++;
        	}
        }
        int[] requiredInt = new int[requiredArray.length-empty];
        int offset = 0;
        for(int k=0; k < requiredArray.length; k++){
        	if(requiredArray[k].isEmpty()){
        		offset++;
        	}else{
        		requiredInt[k-offset]= Integer.parseInt(requiredArray[k]);
        	}
        }
          algo.setMustAppearItems(requiredInt);
        }
        

        
        ArrayList<String[]> outputData = algo.runAlgorithm(inData, m_colsel.getColumnName(), m_minsup.getDoubleValue() , m_showsequence.getBooleanValue());


        
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
        m_minpattern.saveSettingsTo(settings);
        m_maxpattern.saveSettingsTo(settings);
        m_requireditems.saveSettingsTo(settings);
        m_maxgap.saveSettingsTo(settings);
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
        m_minpattern.loadSettingsFrom(settings);
        m_maxpattern.loadSettingsFrom(settings);
        m_requireditems.loadSettingsFrom(settings);
        m_maxgap.loadSettingsFrom(settings);
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
        m_minpattern.validateSettings(settings);
        m_maxpattern.validateSettings(settings);
        m_requireditems.validateSettings(settings);
        m_maxgap.validateSettings(settings);
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
    
    
    public AlgoCMSPAM getAlgo(){
    	return algo;
    }

}

