package org.sequential;

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
 * <code>NodeDialog</code> for the "CMSPAM" Node.
 * Implementation of the CM-SPAM Algorithm for mining frequent sequential patterns
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Dennis S
 */
public class CMSPAMNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring CMSPAM node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    @SuppressWarnings("unchecked")
	protected CMSPAMNodeDialog() {
        super();
        
        setDefaultTabTitle("Standard Options");
        
        addDialogComponent(new DialogComponentNumber(
                new SettingsModelDoubleBounded(
                    CMSPAMNodeModel.CFGKEY_MINSUP,
                    CMSPAMNodeModel.DEFAULT_MINSUP,
                    0.01, 1),
                    "Minsup:", /*step*/ 0.01, /*componentwidth*/ 5));
        
        
        
        addDialogComponent(new DialogComponentColumnNameSelection(
        		new SettingsModelColumnName(
        				CMSPAMNodeModel.CFGKEY_COLSEL,
        				CMSPAMNodeModel.DEFAULT_COLSEL),
        				"Sequence column:",
        				0 ,
        				true,
        				StringValue.class));
        
        
        
       createNewTab("Advanced Options");
        
        
        addDialogComponent(new DialogComponentNumber(
                new SettingsModelIntegerBounded(
                    CMSPAMNodeModel.CFGKEY_MINPATTERN,
                    CMSPAMNodeModel.DEFAULT_MINPATTERN,
                    1, Integer.MAX_VALUE),
                    "Min. pattern length:", /*step*/ 1, /*componentwidth*/ 5));
        
        addDialogComponent(new DialogComponentNumber(
                new SettingsModelIntegerBounded(
                    CMSPAMNodeModel.CFGKEY_MAXPATTERN,
                    CMSPAMNodeModel.DEFAULT_MAXPATTERN,
                    0, Integer.MAX_VALUE),
                    "Max. pattern length: (0 for infinite)", /*step*/ 1, /*componentwidth*/ 5));
        
        
        addDialogComponent(new DialogComponentString(
        		new SettingsModelString(
        				CMSPAMNodeModel.CFGKEY_REQUIREDITEMS,
        				CMSPAMNodeModel.DEFAULT_REQUIREDITEMS),
        				"Required items (e.g. 1,2,3)"));
        
        
        addDialogComponent(new DialogComponentNumber(
                new SettingsModelIntegerBounded(
                    CMSPAMNodeModel.CFGKEY_MAXGAP,
                    CMSPAMNodeModel.DEFAULT_MAXGAP,
                    0, Integer.MAX_VALUE),
                    "Max. gap: (0 for infinite)", /*step*/ 1, /*componentwidth*/ 5));
        
        addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				CMSPAMNodeModel.CFGKEY_SHOWSEQUENCE,
        				CMSPAMNodeModel.DEFAULT_SHOWSEQUENCE),
        				"Show Sequence ids"));
        


                    
    }
}

