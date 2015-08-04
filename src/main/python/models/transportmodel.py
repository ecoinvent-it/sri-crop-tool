from enum import Enum

class TransportDataset(Enum):
    wfldb_transport_lorry_sup_16t="wfldb_transport_lorry_sup_16t"
    wfldb_transport_transoceanic="wfldb_transport_transoceanic"
    wfldb_transport_freight_rail_RER="wfldb_transport_freight_rail_RER"
    wfldb_transport_freight_rail_diesel_US="wfldb_transport_freight_rail_diesel_US"
    ecoinvent_transport_van_inf_3_5t="ecoinvent_transport_van_inf_3_5t"
    ecoinvent_transport_lorry_sup_16t="ecoinvent_transport_lorry_sup_16t"
    ecoinvent_transport_barge="ecoinvent_transport_barge"
    ecoinvent_transport_freight_rail="ecoinvent_transport_freight_rail"

class TransportModel(object):
    """Inputs:
      country: code
      total_composttype: kg / ha
      total_sewagesludge: kg / ha
      nitrogen_from_mineral_fert: kg / ha
      p2O5_from_mineral_fert: kg / ha
      k2O_from_mineral_fert: kg / ha
      magnesium_from_fertilizer: kg / ha
      ca_from_mineral_fert: kg / ha
      pest_total: g / ha
      pest_horticultural_oil: kg / ha
      seeds: kg / ha
      
    Outputs:
      map TransportDataset -> tkm / ha
    """
    
    #NOTE: omit manure transport for the moment
    #NOTE: omit transport of seedlings and planted trees for the moment
    _input_variables = ["country",
                        "total_composttype",
                        "total_sewagesludge",
                        "nitrogen_from_mineral_fert",
                        "p2O5_from_mineral_fert",
                        "k2O_from_mineral_fert",
                        "magnesium_from_fertilizer",
                        "ca_from_mineral_fert",
                        "pest_total",
                        "pest_horticultural_oil",
                        "seeds"
                       ]            
    
    _ORG_FERT_TRANSPORT_DEFAULT_KM = {TransportDataset.wfldb_transport_lorry_sup_16t: 50.0}
    
    _MINERAL_FERT_TRANSPORT_DEFAULT_KM = {
                             TransportDataset.wfldb_transport_lorry_sup_16t: 300.0,
                             TransportDataset.wfldb_transport_transoceanic: 5000.0,
                             TransportDataset.wfldb_transport_freight_rail_RER: 500.0,# not for US/CA
                             TransportDataset.wfldb_transport_freight_rail_diesel_US: 500.0#for US/CA only
                             }
    
    _PESTICIDES_TRANSPORT_DEFAULT_KM = {
                             TransportDataset.wfldb_transport_lorry_sup_16t: 500.0,
                             TransportDataset.wfldb_transport_transoceanic: 5000.0
                             }
    
    _SEEDS_TRANSPORT_DEFAULT_KM = {TransportDataset.wfldb_transport_lorry_sup_16t: 300.0}
    
    _ECOINVENT_COMPOST_TRANSPORT_DEFAULT_KM = {TransportDataset.ecoinvent_transport_van_inf_3_5t: 15.0}
    _ECOINVENT_SEWAGE_SLUDGE_TRANSPORT_DEFAULT_KM = {TransportDataset.ecoinvent_transport_lorry_sup_16t: 15.0}
    
    _ECOINVENT_N_FERT_TRANSPORT_DEFAULT_KM = {
                                              TransportDataset.ecoinvent_transport_barge: 900.0,
                                              TransportDataset.ecoinvent_transport_freight_rail: 100.0,
                                              TransportDataset.ecoinvent_transport_lorry_sup_16t: 100.0
                                              }
    
    _ECOINVENT_P_FERT_TRANSPORT_DEFAULT_KM = {
                                              TransportDataset.ecoinvent_transport_barge: 400.0,
                                              TransportDataset.ecoinvent_transport_freight_rail: 100.0,
                                              TransportDataset.ecoinvent_transport_lorry_sup_16t: 100.0
                                              }
    
    _ECOINVENT_K_FERT_TRANSPORT_DEFAULT_KM = {
                                              TransportDataset.ecoinvent_transport_barge: 100.0,
                                              TransportDataset.ecoinvent_transport_freight_rail: 100.0,
                                              TransportDataset.ecoinvent_transport_lorry_sup_16t: 100.0
                                              }
    
    _ECOINVENT_OTHER_MIN_FERT_TRANSPORT_DEFAULT_KM = {
                                              TransportDataset.ecoinvent_transport_freight_rail: 120.0,
                                              TransportDataset.ecoinvent_transport_lorry_sup_16t: 120.0
                                              }
    
    _ECOINVENT_PESTICIDES_TRANSPORT_DEFAULT_KM = {TransportDataset.ecoinvent_transport_van_inf_3_5t: 30.0}
    
    _ECOINVENT_SEEDS_TRANSPORT_DEFAULT_KM = {TransportDataset.ecoinvent_transport_van_inf_3_5t: 15.0}
    
    _DILUTION_FACTOR = 2.0;
    _ECOINVENT_FERT_DILUTION_FACTOR = 2.0;
    _ECOINVENT_PEST_DILUTION_FACTOR = 3.0;
    
    def __init__(self, inputs):
        #TODO: Should we log usage of default value?
        for key in TransportModel._input_variables:
            setattr(self, key, inputs[key])
    
    def compute(self):
        total_transports = {}
        self.compute_for_wfldb(total_transports)
        self.compute_for_ecoinvent(total_transports)
        return total_transports;
    
    def compute_for_wfldb(self, total_transports):
        self._add_transport_types_for_wfldb(total_transports);
        organic_fert_total = self._compute_organic_fert_total_in_t();
        self._add_transport_for_quantity_in_t(total_transports, organic_fert_total, self._ORG_FERT_TRANSPORT_DEFAULT_KM);
        mineral_fert_total = self._compute_mineral_fert_total_in_t();        
        self._add_transport_for_quantity_in_t(total_transports, mineral_fert_total * self._DILUTION_FACTOR, self._MINERAL_FERT_TRANSPORT_DEFAULT_KM);
        self._add_transport_for_quantity_in_t(total_transports, self.pest_total * self._DILUTION_FACTOR / 1000000.0 + self.pest_horticultural_oil / 1000.0, self._PESTICIDES_TRANSPORT_DEFAULT_KM);
        self._add_transport_for_quantity_in_t(total_transports, self.seeds / 1000.0, self._SEEDS_TRANSPORT_DEFAULT_KM);

    def _add_transport_types_for_wfldb(self, total_transports):
        total_transports[TransportDataset.wfldb_transport_lorry_sup_16t] = 0.0;
        total_transports[TransportDataset.wfldb_transport_transoceanic] = 0.0;
        
        if ( self.country == "US" or self.country == "CA"):
            total_transports[TransportDataset.wfldb_transport_freight_rail_diesel_US] = 0.0
        else: 
            total_transports[TransportDataset.wfldb_transport_freight_rail_RER] = 0.0
            
    def _compute_organic_fert_total_in_t(self): #Manure is considered as produced on site, so no transport for manure
        return (self.total_composttype + self.total_sewagesludge) / 1000.0;
    
    def _compute_mineral_fert_total_in_t(self):
        return (self.nitrogen_from_mineral_fert \
            + self.p2O5_from_mineral_fert \
            + self.k2O_from_mineral_fert \
            + self.magnesium_from_fertilizer \
            + self.ca_from_mineral_fert) / 1000.0;
    
    def compute_for_ecoinvent(self, total_transports):
        self._add_transport_types_for_ecoinvent(total_transports);
        self._add_transport_for_quantity_in_t(total_transports, self.total_composttype / 1000.0, self._ECOINVENT_COMPOST_TRANSPORT_DEFAULT_KM);
        self._add_transport_for_quantity_in_t(total_transports, self.total_sewagesludge / 1000.0, self._ECOINVENT_SEWAGE_SLUDGE_TRANSPORT_DEFAULT_KM);
   
        self._add_transport_for_quantity_in_t(total_transports, self.nitrogen_from_mineral_fert / 1000.0 * self._ECOINVENT_FERT_DILUTION_FACTOR, self._ECOINVENT_N_FERT_TRANSPORT_DEFAULT_KM);
        self._add_transport_for_quantity_in_t(total_transports, self.p2O5_from_mineral_fert / 1000.0 * self._ECOINVENT_FERT_DILUTION_FACTOR, self._ECOINVENT_P_FERT_TRANSPORT_DEFAULT_KM);
        self._add_transport_for_quantity_in_t(total_transports, self.k2O_from_mineral_fert / 1000.0 * self._ECOINVENT_FERT_DILUTION_FACTOR, self._ECOINVENT_K_FERT_TRANSPORT_DEFAULT_KM);
        self._add_transport_for_quantity_in_t(total_transports, (self.magnesium_from_fertilizer + self.ca_from_mineral_fert) / 1000.0 * self._ECOINVENT_FERT_DILUTION_FACTOR, self._ECOINVENT_OTHER_MIN_FERT_TRANSPORT_DEFAULT_KM);

        self._add_transport_for_quantity_in_t(total_transports, self.pest_total * self._ECOINVENT_PEST_DILUTION_FACTOR / 1000000.0 + self.pest_horticultural_oil / 1000.0, self._ECOINVENT_PESTICIDES_TRANSPORT_DEFAULT_KM);
        self._add_transport_for_quantity_in_t(total_transports, self.seeds / 1000.0, self._ECOINVENT_SEEDS_TRANSPORT_DEFAULT_KM);
        
    def _add_transport_types_for_ecoinvent(self, total_transports):
        total_transports[TransportDataset.ecoinvent_transport_van_inf_3_5t] = 0.0
        total_transports[TransportDataset.ecoinvent_transport_lorry_sup_16t] = 0.0
        total_transports[TransportDataset.ecoinvent_transport_barge] = 0.0
        total_transports[TransportDataset.ecoinvent_transport_freight_rail] = 0.0
    
        
    def _add_transport_for_quantity_in_t(self, total_transports, quantity_in_t, transports_map):
        for transportKey in total_transports.keys():
            if (transportKey in transports_map):
                total_transports[transportKey] += transports_map[transportKey] * quantity_in_t
