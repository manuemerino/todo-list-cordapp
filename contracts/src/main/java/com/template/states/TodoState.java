package com.template.states;

import com.template.contracts.TodoContract;
import net.corda.core.contracts.*;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

// *********
// * State *
// *********
@BelongsToContract(TodoContract.class)
public class TodoState implements ContractState, LinearState {

    private final Party assignedBy;
    private final Party assignedTo;
    private final String taskDescription;
    private final Date dateOfCreation;
    private final UniqueIdentifier linearId;


    public TodoState(Party assignedBy,
                     Party assignedTo,
                     String taskDescription) {
        this.assignedBy = assignedBy;
        this.assignedTo = assignedTo;
        this.taskDescription = taskDescription;
        dateOfCreation = new Date();
        linearId = new UniqueIdentifier();
    }

    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(assignedBy, assignedTo);
    }


    @NotNull
    @Override
    public UniqueIdentifier getLinearId() {
        return linearId;
    }

    public Party getAssignedBy () { return assignedBy;}
    public Party getAssignedTo () { return  assignedTo;}
    public String getTaskDescription () {return taskDescription;}

}