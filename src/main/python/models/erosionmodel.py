import math

from enum import Enum

class TillageMethod(Enum):
    fall_plaw="fall_plaw"
    spring_plow="spring_plow"
    mulch_tillage="mulch_tillage"
    ridge_tillage="ridge_tillage"
    zone_tillage="zone_tillage"
    no_till="no_till"
    unknown="unknown"
    
class AntiErosionPractice(Enum):
    no_practice="no_practice"
    cross_slope="cross_slope"
    contour_farming="contour_farming"
    strip_cropping_cross_slope="strip_cropping_cross_slope"
    strip_cropping_contour="strip_cropping_contour"
    unknown="unknown"
    
#FIXME: Irrigation per crop cycle
class ErosionModel(object):
    """Inputs:
      average_annual_precipitation: mm/year
      yearly_precipitation_as_snow: ratio
      annualized_irrigation: mm/year
      slope: ratio #default: 0% if rice, 3% for other
      slope_length: m
      tillage_method: TillageMethod
      anti_erosion_practice: AntiErosionPractice
      soil_erodibility_factor: t h/(MJ*mm)
      crop_factor: number
      
    Outputs:
      m_Erosion_eroded_soil: kg soil/(ha*year)
    """
    
    _input_variables = ["average_annual_precipitation",
                        "yearly_precipitation_as_snow",
                        "annualized_water_use_total",
                        "slope",
                        "slope_length",
                        "tillage_method",
                        "anti_erosion_practice",
                        "soil_erodibility_factor",
                        "crop_factor"
                       ]
    
    _POW_FOR_SLOPE_UNDER_1_PERCENT = 0.2;
    _POW_FOR_SLOPE_1_TO_3_5_PERCENT = 0.3;
    _POW_FOR_SLOPE_3_5_TO_5_PERCENT = 0.4;
    _POW_FOR_SLOPE_OVER_5_PERCENT = 0.5;
    
    #FIXME: match with input crop
    # put in input module
    # Complete with GD_crop / Cfactor_L1 ; même principe (Prinzip Hoffnung)
    _CROP_FACTOR = { "Jatropha":0.2,
                    "Sunflower":0.5,
                    "Wheat":0.35,
                    "Castor bean":0.5,
                        
                    "Potato":0.4,
                    "Sugar beet":0.5,
                    "Sugar cane":0.3,
                    "Sweet sorghum":0.4,
                    "Rapeseed":0.5,
                    "Soya":0.4,
                    "Palm":0.2};
                    
    _TILLAGE_METHOD_FACTOR = {TillageMethod.unknown:1.0,
                                TillageMethod.fall_plaw:1.0,
                                TillageMethod.spring_plow:0.9,
                                TillageMethod.mulch_tillage:0.6,
                                TillageMethod.ridge_tillage:0.35,
                                TillageMethod.zone_tillage:0.25,
                                TillageMethod.no_till:0.25};
                        
    _ANTI_EROSION_PRACTICE_FACTOR = { AntiErosionPractice.unknown:1,
                                        AntiErosionPractice.no_practice:1,
                                        AntiErosionPractice.cross_slope:0.75,
                                        AntiErosionPractice.contour_farming:0.5,
                                        AntiErosionPractice.strip_cropping_cross_slope:0.37,
                                        AntiErosionPractice.strip_cropping_contour:0.25};

    
    def __init__(self, inputs):
        #TODO: Should we log usage of default value?
        for key in ErosionModel._input_variables:
            setattr(self, key, inputs[key])
    
    def compute(self):
        erosivity_factor = self._compute_erosivity_factor();
        slope_factor = self._compute_slope_factor();
        tillage_factor = self._compute_tillage_factor();
        practice_factor = self._compute_practice_factor();
        eroded_soil = 1000.0 * erosivity_factor * self.soil_erodibility_factor \
                     * slope_factor * self.crop_factor * tillage_factor * practice_factor;
        return {"m_Erosion_eroded_soil": eroded_soil}
        
    def _compute_erosivity_factor(self):#MJ mm/(ha*h*yr)
        precipitation_factor = self._compute_precipitation_factor();
        if (precipitation_factor <=850.0):
            return 0.0483 * precipitation_factor**1.61;
        else:
            return 587.8 - 1.219 * precipitation_factor + 0.004105 * precipitation_factor**2;
    
    def _compute_precipitation_factor(self):#FIXME: check if we have to add or remove the second part
        return self.average_annual_precipitation * (1.0 - self.yearly_precipitation_as_snow) \
                + 0.1 * ((self.annualized_water_use_total * 0.1) + self.yearly_precipitation_as_snow * self.average_annual_precipitation);
    
    def _compute_slope_factor(self):
        return (self.slope_length * 3.28083/72.6)**self._compute_pow_for_slope_factor() * (65.41*(math.sin(self.slope))**2 \
                + 4.56*(math.sin(self.slope)) + 0.065);
    
    def _compute_pow_for_slope_factor(self):
        if (self.slope < 0.01):
            return self._POW_FOR_SLOPE_UNDER_1_PERCENT;
        elif (self.slope < 0.035):
            return self._POW_FOR_SLOPE_1_TO_3_5_PERCENT;
        elif (self.slope < 0.05):
            return self._POW_FOR_SLOPE_3_5_TO_5_PERCENT;
        else:
            return self._POW_FOR_SLOPE_OVER_5_PERCENT;
        
    def _compute_tillage_factor(self):
        return self._TILLAGE_METHOD_FACTOR[self.tillage_method];
    
    def _compute_practice_factor(self):
        return self._ANTI_EROSION_PRACTICE_FACTOR[self.anti_erosion_practice];
        