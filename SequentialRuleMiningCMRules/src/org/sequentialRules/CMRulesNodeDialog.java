package org.sequentialRules;

import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;

/**
 * <code>NodeDialog</code> for the "CMRules" Node.
 * Implementation of the CMRules algorithm
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Dennis S.
 */
public class CMRulesNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring CMRules node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected CMRulesNodeDialog() {
        super();
        
        
        addDialogComponent(new DialogComponentNumber(
                new SettingsModelDoubleBounded(
                    CMRulesNodeModel.CFGKEY_MINSUP,
                    CMRulesNodeModel.DEFAULT_MINSUP,
                    0.01, 1),
                    "Minsup:", /*step*/ 0.01, /*componentwidth*/ 5));
        
        
        addDialogComponent(new DialogComponentNumber(
                new SettingsModelDoubleBounded(
                    CMRulesNodeModel.CFGKEY_MINCONF,
                    CMRulesNodeModel.DEFAULT_MINCONF,
                    0.01, 1),
                    "Minconf:", /*step*/ 0.01, /*componentwidth*/ 5));
        
        
        
        
        addDialogComponent(new DialogComponentColumnNameSelection(
        		new SettingsModelColumnName(
        				CMRulesNodeModel.CFGKEY_COLSEL,
        				CMRulesNodeModel.DEFAULT_COLSEL),
        				"Sequence column:",
        				0 ,
        				true,
        				StringValue.class));

       
                    
    }
}

