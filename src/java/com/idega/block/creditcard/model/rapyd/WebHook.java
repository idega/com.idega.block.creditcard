package com.idega.block.creditcard.model.rapyd;

import com.idega.block.creditcard.CreditCardConstants;
import com.idega.block.creditcard.model.WebHookable;

public class WebHook implements WebHookable {

	private static final long serialVersionUID = 3206199204544325498L;

	private String id;

    private String type;

    private Data data;

    private String trigger_operation_id;

    private String status;

    private int created_at;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public String getTrigger_operation_id() {
		return trigger_operation_id;
	}

	public void setTrigger_operation_id(String trigger_operation_id) {
		this.trigger_operation_id = trigger_operation_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCreated_at() {
		return created_at;
	}

	public void setCreated_at(int created_at) {
		this.created_at = created_at;
	}

	@Override
	public String toString() {
		return CreditCardConstants.GSON.toJson(this);
	}

}