package org.sequentialRules;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "CMRules" Node.
 * Implementation of the CMRules algorithm
 *
 * @author Dennis S.
 */
public class CMRulesNodeFactory 
        extends NodeFactory<CMRulesNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CMRulesNodeModel createNodeModel() {
        return new CMRulesNodeModel();
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
    public NodeView<CMRulesNodeModel> createNodeView(final int viewIndex,
            final CMRulesNodeModel nodeModel) {
        return new CMRulesNodeView(nodeModel);
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
        return new CMRulesNodeDialog();
    }

}

