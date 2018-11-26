package com.quantis_intl.lcigenerator.ecospold;

import java.util.UUID;

public enum AvailableUnit
{
    KG_PER_MJ("c182d036-038d-4c66-86ba-280c8a624ddf", "kg/MJ"),
    KG_PER_HOUR("a309a95f-0ed9-4672-8749-2dc7a190c82c", "kg/hour"),
    SECOND("a5d2899a-ea3f-4810-a530-a26fff736aac", "s"),
    M2_PER_KG("35dc944d-443f-48a5-a2fd-e310c3e11cf5", "m2/kg"),
    M_YEAR("8266bd67-ebd3-4d31-b972-eee3cf0da36f", "m*year"),
    PERSON_KM("0e6f38bc-51be-4382-802d-1f389a88e589", "person*km"),
    KBQ("4923348e-591b-4772-b224-d19df86f04c9", "kBq"),
    KW("7de7e78f-ccf0-46b4-a549-20b04f4b1421", "kW"),
    L("86b0e4a2-57e5-48b8-a43d-22c7f8490dca", "l"),
    M2("1017f68a-f818-4f9d-a34d-a31e7386f628", "m2"),
    T_KM("2f4daad7-5331-4f14-930c-d8bca924557d", "metric ton*km"),
    KM_PER_HOUR("33964fe2-6f44-4ed6-844d-b29dea69aad0", "km/hour"),
    M3_PER_KWH("d8c254d2-9ad0-4e1f-a220-58d1db66c21c", "m3/kWh"),
    K("f003f001-61c7-4713-9111-1516a6e6f772", "K"),
    KJ_PER_KG("01487f54-aed2-4e40-bfb9-758979136238", "kJ/kg"),
    M3("de5b3c87-0e35-4fb0-9765-4f3ba34c99e5", "m3"),
    KG_PER_M2("b9c5482c-c0d1-4012-b7ef-84a758d7ad03", "kg/m2"),
    KG_PER_L("b06bab82-f0e8-426d-849a-ebdb5b553519", "kg/l"),
    L_PER_H_M2("201adf23-de8c-4e1d-a658-01fd75ba872d", "l/(h.m2)"),
    MJ("980b811e-3905-4797-82a5-173f5568bc7e", "MJ"),
    M2_YEAR("eb955b7c-7bed-401f-9c76-5db716ca3640", "m2*year"),
    T_PER_HA_YEAR("c1520f88-5ae0-4670-b39a-aff30e984b0b", "metric ton/(ha*year)"),
    KG_PER_M3("cacb6d36-694d-4e4f-9e79-6c9c73146839", "kg/m3"),
    KG_PER_KWH("aa0d3bf3-b9d6-4ba2-b552-9e8d0ce52408", "kg/kWh"),
    KM_YEAR("08bd0c61-0b9a-4280-bc25-3444d52c15bf", "km*year"),
    KJ_PER_KCAL("f4ade0ac-636a-41bb-b1d9-17e78a67fa07", "MJ/kcal"),
    HA("86bbe475-8a8f-44d8-914c-e398787e7121", "ha"),
    KWH_PER_SECOND("b61bad7c-10e1-4f69-88f6-440a063ece33", "kWh/s"),
    HOUR("e32b56ef-fa80-4487-9796-f3c1476c27b3", "hour"),
    HOUR_PER_YEAR("54f32be9-4db9-4c17-9f63-8a712fca56f7", "hour/year"),
    MM_PER_YEAR("02c7d6d0-cca5-4b23-a5b8-1b77f1d3ec83", "mm/year"),
    KG_PER_M("c26ec641-222e-42e5-b8f8-4d96ec8ea27e", "kg/m"),
    KG("487df68b-4994-4027-8fdc-a4dc298257b7", "kg"),
    KM("ae252091-811b-461b-8e89-8f3075639eb1", "km"),
    T_DAY("07eff02f-075c-4dde-9b6d-348d534877d5", "metric ton*day"),
    DAY("05af3809-c874-4611-a6e9-6c296385ec5d", "day"),
    KWH("77ae64fa-7e74-4252-9c3b-889c1cd20bfc", "kWh"),
    BTU_PER_KG("c4d1b1ec-1ddb-4800-87db-ffbfbb29d94f", "BTU/kg"),
    KG_PER_M2_YEAR("4292d301-1096-447a-988b-e67ab0f5f3e2", "kg/(m2*year)"),
    L_PER_M3("56e0f502-7bef-4678-88a9-0cb2a8fa8e63", "l/m3"),
    KG_PER_HA("395f1718-3067-449c-ad6a-b96e876eebd4", "kg/ha"),
    KWH_PER_YEAR("46a269fc-4efb-4087-b5c1-9f4bc318275d", "kWh/year"),
    MJ_PER_KWH("34e7dd21-3518-40a5-8dbd-5e1bdf888620", "MJ/kWh"),
    T_PER_HA("fc5f66f9-d510-4bdb-9ee2-ae54736546ca", "metric ton/ha"),
    M("f2aaa4ba-fb58-49a7-a552-e7f64366f379", "m"),
    KWH_PER_KM("848cdafe-0725-4376-b427-728171c79d40", "kWh/km"),
    MW("9dbdef14-e661-41e7-a878-e1f380b24d5a", "MW"),
    KG_PER_GJ("2a448677-87c7-4ccf-953a-8d71c73a4c04", "kg/GJ"),
    YEAR("2cf92850-0f92-4004-9dba-a6ceb6a414c2", "year"),
    MJ_PER_M3("9d26a1d3-0c5c-4bb9-98b1-3125e247bc21", "MJ/m3"),
    KG_PER_SECOND("3ce424af-a3eb-4c16-9bc8-aa4443c94f50", "kg/s"),
    MM("3e2c67b8-bc67-42a5-bc81-00756f0e05bb", "mm"),
    KCAL("be283e24-26b9-44df-9596-f1cb9db28a53", "kcal"),
    MJ_PER_KG("34e6691f-4a1b-4c2b-b239-c55b952f0bd3", "MJ/kg"),
    UNIT("5b972631-34e3-4db7-a615-f6931770a0cb", "unit"),
    KG_DAY("241450a3-3497-4648-8c68-e44d04f64ac7", "kg*day"),
    M2_PER_YEAR("2707f8e2-f546-43ca-b0af-850d438b5dda", "m2/year"),
    KG_PER_UNIT("06477fb5-7177-491d-8383-aa6c81614c5c", "kg/unit"),
    L_PER_M2("c1b47c9a-650a-4bf8-bcbc-ed02e7698fe2", "l/m2"),
    M3_YEAR("481b9712-c417-44f1-bfba-38d58088173c", "m3*year"),
    M3_PER_KG("15a259d2-4417-45e8-9f67-7459326c5eb2", "m3/kg"),
    L_PER_HOUR("bc240401-c632-4b0f-9fc3-945610d47fc5", "l/hour"),
    KG_PER_HA_YEAR("1eb913ab-3676-4615-bb13-2bcfc37e5e85", "kg/ha.yr"),
    KG_PER_YEAR("a3a82f71-552b-4552-82eb-f241e722696f", "kg/year"),
    M2_PER_HA("8ee4b882-0429-47dc-883c-3961cef82e85", "m2/ha"),
    M3_PER_HA("09e8c86e-1d3a-430e-aad0-fa8cc5e15b66", "m³/ha");

    public final UUID uuid;
    public final String symbol;

    AvailableUnit(String uuid, String symbol)
    {
        this.uuid = UUID.fromString(uuid);
        this.symbol = symbol;
    }
}
