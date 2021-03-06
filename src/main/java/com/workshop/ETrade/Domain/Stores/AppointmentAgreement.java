package com.workshop.ETrade.Domain.Stores;

import java.util.Map;

public class AppointmentAgreement {

    private Map<String, Boolean> awaitingApproval;

    private boolean isRejected;

    public AppointmentAgreement(Map<String, Boolean> awaitingApproval) {
        this.awaitingApproval = awaitingApproval;
    }

    public boolean isApproved() {
        for(String owner : awaitingApproval.keySet()) {
            if(!awaitingApproval.get(owner)) {
                return false;
            }
        }
        return true;
    }

    public String approve(String approvedBy, boolean approve) {
        if(!approve) {
            isRejected = true;
        } else  {
            if(!awaitingApproval.containsKey(approvedBy)) {
                return approvedBy + " is not on the waiting list";
            }
            awaitingApproval.computeIfPresent(approvedBy, (K,V) -> V = true);
            if(isApproved()) {
                return approvedBy + " is now a store owner";
            } else {
                return approvedBy + " is still waiting for more owners to approve";
            }
        }
        return "approval of " + approve + " has been rejected";
    }

    public Map<String, Boolean> getWaiting() {
        return awaitingApproval;
    }
}
