package com.dominator.enums;

public enum BillEnums {

    PMSSAAS_WATER_BILL("水费"),
    PMSSAAS_ELECTRIC_CHARGE_BILL("电费"),
    PMSSAAS_GAS_BILL("燃气费"),
    PMSSAAS_RENT_BILL("房租费"),
    PMSSAAS_PROPERTY_BILL("物业费")
    ;


    /**
     * msg
     */
    private String msg;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private BillEnums(String msg) {
        this.msg = msg;
    }
}
