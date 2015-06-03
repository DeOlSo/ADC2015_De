package org.closedSequential;

import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.util.ColumnFilter;


/**
 * <code>NodeDialog</code> for the "BIDE" Node.
 * Implementation of the BIDE Algorithm for mining closed frequent sequential patterns
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Dennis S
 */
public class BIDENodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring CMSPAM node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    @SuppressWarnings("unchecked")
	protected BIDENodeDialog() {
        super();
        
        
        addDialogComponent(new DialogComponentNumber(
                new SettingsModelDoubleBounded(
                    BIDENodeModel.CFGKEY_MINSUP,
                    BIDENodeModel.DEFAULT_MINSUP,
                    0.01, 1),
                    "Minsup:", /*step*/ 0.01, /*componentwidth*/ 5));
        
        
        
        addDialogComponent(new DialogComponentColumnNameSelection(
        		new SettingsModelColumnName(
        				BIDENodeModel.CFGKEY_COLSEL,
        				BIDENodeModel.DEFAULT_COLSEL),
        				"Sequence column:",
        				0 ,
        				true,
        				StringValue.class));

        
        addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				BIDENodeModel.CFGKEY_SHOWSEQUENCE,
        				BIDENodeModel.DEFAULT_SHOWSEQUENCE),
        				"Show Sequence ids"));
        


                    
    }
}

