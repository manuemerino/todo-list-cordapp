package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.google.common.collect.ImmutableList;
import com.template.contracts.TodoContract;
import com.template.states.TodoState;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.node.ServiceHub;
import net.corda.core.node.services.IdentityService;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import net.corda.core.utilities.UntrustworthyData;

// ******************
// * TodoInitiator flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class TodoInitiator extends FlowLogic<Void> {
    private final ProgressTracker progressTracker = new ProgressTracker();
    private final String taskDescription;

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    public TodoInitiator(String taskDescription) {
        this.taskDescription = taskDescription;
    }
    @Suspendable
    @Override
    public Void call() throws FlowException {
        // TodoInitiator flow logic goes here.
        Party myIdentity = getOurIdentity();
        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
        TodoState todoState = new TodoState(myIdentity,myIdentity,taskDescription);
        TransactionBuilder utx = new TransactionBuilder(notary)
                                        .addOutputState(todoState, TodoContract.ID)
                                        .addCommand(new TodoContract.Commands.Create(),
                                                todoState.getAssignedBy().getOwningKey());
        SignedTransaction stx = getServiceHub().signInitialTransaction(utx);
        subFlow(new FinalityFlow(stx));
        System.out.println("Task: \""+todoState.getTaskDescription()+"\" created");
        return null;
    }
}
