from enum import Enum

class TransportDataset(Enum):
    transport_lorry_sup_16t_fleet_average_RER="transport_lorry_sup_16t_fleet_average_RER"
    transport_transoceanic_freight_ship_OCE="transport_transoceanic_freight_ship_OCE"
    transport_freight_rail_RER="transport_freight_rail_RER"
    transport_freight_rail_diesel_US="transport_freight_rail_diesel_US"

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
                        "seeds"
                       ]            
    
    _ORG_FERT_TRANSPORT_DEFAULT_KM = {TransportDataset.transport_lorry_sup_16t_fleet_average_RER: 50.0}
    
    _MINERAL_FERT_TRANSPORT_DEFAULT_KM = {
                             TransportDataset.transport_lorry_sup_16t_fleet_average_RER: 300.0,
                             TransportDataset.transport_transoceanic_freight_ship_OCE: 5000.0,
                             TransportDataset.transport_freight_rail_RER: 500.0,# not for US/CAN
                             TransportDataset.transport_freight_rail_diesel_US: 500.0#for US/CAN only
                             }
    
    _PESTICIDES_TRANSPORT_DEFAULT_KM = {
                             TransportDataset.transport_lorry_sup_16t_fleet_average_RER: 500.0,
                             TransportDataset.transport_transoceanic_freight_ship_OCE: 5000.0
                             }
    
    _SEEDS_TRANSPORT_DEFAULT_KM = {TransportDataset.transport_lorry_sup_16t_fleet_average_RER: 300.0}
    
    _DILUTION_FACTOR = 2.0;
    
    def __init__(self, inputs):
        #TODO: Should we log usage of default value?
        for key in TransportModel._input_variables:
            setattr(self, key, inputs[key])
    
    def compute(self):
        total_transports = self._init_transports_map();
        organic_fert_total = self._compute_organic_fert_total_in_t();
        self._add_transport_for_quantity_in_t(total_transports, organic_fert_total, self._ORG_FERT_TRANSPORT_DEFAULT_KM);
        mineral_fert_total = self._compute_mineral_fert_total_in_t();        
        self._add_transport_for_quantity_in_t(total_transports, mineral_fert_total * self._DILUTION_FACTOR, self._MINERAL_FERT_TRANSPORT_DEFAULT_KM);
        self._add_transport_for_quantity_in_t(total_transports, self.pest_total * self._DILUTION_FACTOR / 1000000.0, self._PESTICIDES_TRANSPORT_DEFAULT_KM);
        self._add_transport_for_quantity_in_t(total_transports, self.seeds / 1000.0, self._SEEDS_TRANSPORT_DEFAULT_KM);
        return {"m_transport_transport_tkm":total_transports};

    def _init_transports_map(self):
        transports = {TransportDataset.transport_lorry_sup_16t_fleet_average_RER: 0.0,
                      TransportDataset.transport_transoceanic_freight_ship_OCE: 0.0}
        if ( self.country == "US" or self.country == "CAN"):
            transports[TransportDataset.transport_freight_rail_diesel_US] = 0.0
        else: 
            transports[TransportDataset.transport_freight_rail_RER] = 0.0
        return transports;
    
    def _compute_organic_fert_total_in_t(self):
        return (self.total_composttype + self.total_sewagesludge) / 1000.0;
    
    def _compute_mineral_fert_total_in_t(self):
        return (self.nitrogen_from_mineral_fert \
            + self.p2O5_from_mineral_fert \
            + self.k2O_from_mineral_fert \
            + self.magnesium_from_fertilizer \
            + self.ca_from_mineral_fert) / 1000.0;
        
    def _add_transport_for_quantity_in_t(self, total_transports, quantity_in_t, transports_map):
        for transportKey in total_transports.keys():
            if (transportKey in transports_map):
                total_transports[transportKey] += transports_map[transportKey] * quantity_in_t
