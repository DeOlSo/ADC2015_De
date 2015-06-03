package org.closedSequential;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "BIDE" Node.
 * Implementation of the BIDE Algorithm for mining closed frequent sequential patterns
 *
 * @author Dennis S
 */
public class BIDENodeFactory 
        extends NodeFactory<BIDENodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public BIDENodeModel createNodeModel() {
        return new BIDENodeModel();
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
    public NodeView<BIDENodeModel> createNodeView(final int viewIndex,
            final BIDENodeModel nodeModel) {
        return new BIDENodeView(nodeModel);
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
        return new BIDENodeDialog();
    }

}

