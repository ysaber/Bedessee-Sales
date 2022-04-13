package com.thebedesseegroup.sales.update.json.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.thebedesseegroup.sales.salesman.Salesman;
import com.thebedesseegroup.sales.salesmanstore.SalesmanStore;
import com.thebedesseegroup.sales.store.Store;

import java.lang.reflect.Type;

/**
 * TODO: Document me...
 */
public class SalesmanStoreDeserializer implements JsonDeserializer<SalesmanStore> {
    
    
    @Override
    public SalesmanStore deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        final JsonObject jsonObject = json.getAsJsonObject();
        
        final Salesman salesman = new Salesman(jsonObject.get("SALESPERSON").getAsString(), jsonObject.get("SALES_EMAIL").getAsString());

        final Store store = new Store();
        store.setName(jsonObject.get("CUST_OR_SHIP_NAME").getAsString());
        store.setNumber(jsonObject.get("CUST_NUM").getAsString());
        store.setAddress(jsonObject.get("CUST_OR_SHIP_ADDR").getAsString());
        store.setLastCollectDaysOld(jsonObject.get("LAST_COLLECT_DAYS_OLD").getAsString());
        store.setLastCollectDate(jsonObject.get("LAST_COLLECT_DATE").getAsString());
        store.setLastCollectInvoice(jsonObject.get("LAST_COLLECT_INVOICE#").getAsString());
        store.setLastCollectAmount(jsonObject.get("LAST_COLLECT_AMOUNT").getAsString());
        store.setOutstadingBalanceDue(jsonObject.get("OUTSTANDING_BAL_DUE").getAsString());
        store.setShowPopup(jsonObject.get("POP UP STMT PROMPT").getAsString().equals("Y"));

        if (jsonObject.has("STATEMENT FILE NAME")) {
            store.setStatementUrl(jsonObject.get("STATEMENT FILE NAME").getAsString());
        } else if (jsonObject.has("STATEMENT FILE NAME ")) {
            store.setStatementUrl(jsonObject.get("STATEMENT FILE NAME ").getAsString());
        } else if (jsonObject.has("STATEMENT FILE NAME (WEB LINK)")) {
            store.setStatementUrl(jsonObject.get("STATEMENT FILE NAME (WEB LINK)").getAsString());
        }

        return new SalesmanStore(salesman, store);
    }
    
}
