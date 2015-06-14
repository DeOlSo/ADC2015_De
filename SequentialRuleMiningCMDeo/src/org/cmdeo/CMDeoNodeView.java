package org.cmdeo;

import javax.swing.JLabel;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "CMDeo" Node.
 * Implementation of the CMDeo algorithm
 *
 * @author Dennis S.
 */
public class CMDeoNodeView extends NodeView<CMDeoNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link CMDeoNodeModel})
     */
    protected CMDeoNodeView(final CMDeoNodeModel nodeModel) {
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
        CMDeoNodeModel nodeModel = 
            (CMDeoNodeModel)getNodeModel();
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

