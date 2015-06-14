package org.cmdeo;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "CMDeo" Node.
 * Implementation of the CMDeo algorithm
 *
 * @author Dennis S.
 */
public class CMDeoNodeFactory 
        extends NodeFactory<CMDeoNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CMDeoNodeModel createNodeModel() {
        return new CMDeoNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<CMDeoNodeModel> createNodeView(final int viewIndex,
            final CMDeoNodeModel nodeModel) {
        return new CMDeoNodeView(nodeModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new CMDeoNodeDialog();
    }

}

