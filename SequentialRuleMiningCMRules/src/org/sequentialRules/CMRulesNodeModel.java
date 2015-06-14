package org.sequentialRules;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;


import spmf.cmrules.AlgoCMRules;


/**
 * This is the model implementation of CMRules.
 * Implementation of the CMRules algorithm
 *
 * @author Dennis S.
 */
public class CMRulesNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(CMRulesNodeModel.class);
        
    /** the settings key which is used to retrieve and 
        store the settings (from the dialog or from a settings file)    
       (package visibility to be usable from the dialog). */
	static final String CFGKEY_MINSUP = "MinSup";
	static final String CFGKEY_MINCONF = "MinConf";
	static final String CFGKEY_COLSEL = "ColSel";
	

    /** initial default values. */	
    static final double DEFAULT_MINSUP = 0.01;
    static final double DEFAULT_MINCONF = 0.01;
    static final String DEFAULT_COLSEL = "";



    
  
    
    private final SettingsModelDoubleBounded m_minsup =
            new SettingsModelDoubleBounded(
                CMRulesNodeModel.CFGKEY_MINSUP,
                CMRulesNodeModel.DEFAULT_MINSUP,
                0.01, 1);
    
    
    private final SettingsModelDoubleBounded m_minconf =
            new SettingsModelDoubleBounded(
                CMRulesNodeModel.CFGKEY_MINCONF,
                CMRulesNodeModel.DEFAULT_MINCONF,
                0.01, 1);
    


   
   private final SettingsModelColumnName m_colsel =
   		new SettingsModelColumnName(
   				CMRulesNodeModel.CFGKEY_COLSEL,
   				CMRulesNodeModel.DEFAULT_COLSEL);  
   
   
   
   private AlgoCMRules algoCMRules;
    

    /**
     * Constructor for the node model.
     */
    protected CMRulesNodeModel() {
    
        // TODO one incoming port and one outgoing port is assumed
        super(1, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

        algoCMRules = new AlgoCMRules();
        ArrayList<String[]>  outputData = algoCMRules.runAlgorithm(inData, m_colsel.getColumnName(), m_minsup.getDoubleValue() , m_minconf.getDoubleValue());
            
            
        
        // the data table spec of the single output table, 
        // the table will have three columns:
        DataColumnSpec[] allColSpecs = new DataColumnSpec[3];
        allColSpecs[0] = 
            new DataColumnSpecCreator("Rule", StringCell.TYPE).createSpec();
        allColSpecs[1] = 
            new DataColumnSpecCreator("Support (absolute)", IntCell.TYPE).createSpec();
        allColSpecs[2] = 
            new DataColumnSpecCreator("Confidence", DoubleCell.TYPE).createSpec();
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        // the execution context will provide us with storage capacity, in this
        // case a data container to which we will add rows sequentially
        // Note, this container can also handle arbitrary big data tables, it
        // will buffer to disc if necessary.
        BufferedDataContainer container = exec.createDataContainer(outputSpec);

        int rowCount = inData[0].getRowCount();
        for(int k = 0; k < outputData.size(); k++){
        RowKey key = new RowKey("Row " + k);
        DataCell[] cells = new DataCell[3];
        cells[0] = new StringCell(outputData.get(k)[0]); 
        int support = Integer.parseInt(outputData.get(k)[1].replaceAll("[^\\d]", ""));
        cells[1] = new IntCell(support); 
        double confidence = Double.parseDouble(outputData.get(k)[2]);
        cells[2] = new DoubleCell(confidence);

        
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

    	
        m_colsel.saveSettingsTo(settings);
        m_minconf.saveSettingsTo(settings);
        m_minsup.saveSettingsTo(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            

        m_colsel.loadSettingsFrom(settings);
        m_minconf.loadSettingsFrom(settings);
        m_minsup.loadSettingsFrom(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        m_colsel.validateSettings(settings);
        m_minconf.validateSettings(settings);
        m_minsup.validateSettings(settings);

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
    
    
    public AlgoCMRules getAlgo(){
    	return algoCMRules;
    }

}

