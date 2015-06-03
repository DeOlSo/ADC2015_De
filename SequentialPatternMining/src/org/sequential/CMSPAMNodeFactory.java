package org.sequential;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "CMSPAM" Node.
 * Implementation of the CM-SPAM Algorithm for mining frequent sequential patterns
 *
 * @author Dennis S
 */
public class CMSPAMNodeFactory 
        extends NodeFactory<CMSPAMNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CMSPAMNodeModel createNodeModel() {
        return new CMSPAMNodeModel();
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
    public NodeView<CMSPAMNodeModel> createNodeView(final int viewIndex,
            final CMSPAMNodeModel nodeModel) {
        return new CMSPAMNodeView(nodeModel);
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
        return new CMSPAMNodeDialog();
    }

}

