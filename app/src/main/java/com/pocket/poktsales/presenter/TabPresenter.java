package com.pocket.poktsales.presenter;

import com.pocket.poktsales.R;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.MTicket;

import java.util.List;

/**
 * Created by MAV1GA on 09/11/2017.
 */

public class TabPresenter extends BasePresenter implements RequiredPresenterOps.TabPresenterOps {

    private RequiredViewOps.TabViewOps view;

    public TabPresenter (RequiredViewOps.TabViewOps view){
        this.view = view;
    }

    @Override
    public List<MTicket> getAllOpenTabs() {
        return fromTicketList(
                Ticket.find(Ticket.class, "ticket_status = ?", String.valueOf(Ticket.TICKET_PENDING)));
    }

    @Override
    public void openTab(String tabReference) {
        if (!isValid(tabReference)){
            view.onError(R.string.invalid_name_for_tab);
            return;
        }
        if (!isTicketInUse(tabReference)) {
            Ticket ticket = new Ticket();
            ticket.setTicketReference(tabReference);
            ticket.setTicketStatus(Ticket.TICKET_PENDING);
            ticket.save();
            view.onNewTab(fromTicket(ticket));
        }else{
            view.onError(R.string.err_tab_name_already_in_use);
        }
    }

    private boolean isValid(String tabReference){
        return !tabReference.contains("*") && tabReference.length() > 0;
    }
}
