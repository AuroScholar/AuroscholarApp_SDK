package com.auro.application.home.data.model.response;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InstructionsItemModel {

    @SerializedName("insrtuction_id")
    @Expose
    private Integer insrtuctionId;
    @SerializedName("insrtuction_type_id")
    @Expose
    private Integer insrtuctionTypeId;
    @SerializedName("instruction_type_name")
    @Expose
    private String instructionTypeName;
    @SerializedName("instruction_message")
    @Expose
    private String instructionMessage;

    public Integer getInsrtuctionId() {
        return insrtuctionId;
    }

    public void setInsrtuctionId(Integer insrtuctionId) {
        this.insrtuctionId = insrtuctionId;
    }

    public Integer getInsrtuctionTypeId() {
        return insrtuctionTypeId;
    }

    public void setInsrtuctionTypeId(Integer insrtuctionTypeId) {
        this.insrtuctionTypeId = insrtuctionTypeId;
    }

    public String getInstructionTypeName() {
        return instructionTypeName;
    }

    public void setInstructionTypeName(String instructionTypeName) {
        this.instructionTypeName = instructionTypeName;
    }

    public String getInstructionMessage() {
        return instructionMessage;
    }

    public void setInstructionMessage(String instructionMessage) {
        this.instructionMessage = instructionMessage;
    }

}