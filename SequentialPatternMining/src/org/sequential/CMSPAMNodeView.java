package org.sequential;


import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.FlowLayout;
import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "CMSPAM" Node.
 * Implementation of the CM-SPAM Algorithm for mining frequent sequential patterns
 *
 * @author Dennis S
 */
public class CMSPAMNodeView extends NodeView<CMSPAMNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link CMSPAMNodeModel})
     */
    protected CMSPAMNodeView(final CMSPAMNodeModel nodeModel) {
        super(nodeModel);
       
		String s = nodeModel.getAlgo().getStatistics();
		s = "<html>" + s.replaceAll("\n", "<br>");

        setComponent(new JLabel(s));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {

        // TODO retrieve the new model from your nodemodel and 
        // update the view.
        CMSPAMNodeModel nodeModel = 
            (CMSPAMNodeModel)getNodeModel();
        assert nodeModel != null;
        
        // be aware of a possibly not executed nodeModel! The data you retrieve
        // from your nodemodel could be null, emtpy, or invalid in any kind.
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
    
        // TODO things to do when closing the view
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {

        // TODO things to do when opening the view
    }

}

