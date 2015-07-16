MA_H = 1.00794
MA_C = 12.0107
MA_N = 14.00674
MA_O = 15.9994
MA_Mg = 24.3050
MA_P = 30.973762
MA_Ca = 40.078

MA_CO2 = MA_C + MA_O * 2
MA_NO = MA_N + MA_O
MA_NO2 = MA_N + MA_O * 2
MA_NO3 = MA_N + MA_O * 3
MA_N2O = MA_N * 2 + MA_O
MA_NH3 = MA_N + MA_H * 3
MA_PO4 = MA_P + MA_O * 4
MA_UREA = MA_C + MA_H * 4 + MA_N * 2 + MA_O #CH4N2O
MA_LIMESTONE = MA_Ca + MA_C + MA_O * 3 # CaCO3
MA_DOLOMITE = MA_Ca + MA_Mg + (MA_C + MA_O * 3) * 2 #CaMg(CO3)2


N_TO_NO3_FACTOR = MA_NO3 / MA_N
NO_TO_N_FACTOR = MA_N / MA_NO
NO2_TO_N_FACTOR = MA_N / MA_NO2
N_TO_N2O_FACTOR = MA_N2O / MA_N
N_TO_NO2_FACTOR = MA_NO2 / MA_N
NH3_TO_N_FACTOR = MA_N / MA_NH3
N_TO_NH3_FACTOR = MA_NH3 / MA_N
P_TO_PO4_FACTOR = MA_PO4 / MA_P
Ca_TO_LIMESTONE_FACTOR = MA_LIMESTONE / MA_Ca
LIMESTONE_TO_CO2_FACTOR = MA_CO2 / MA_LIMESTONE 
MG_TO_DOLOMITE_FACTOR = MA_DOLOMITE / MA_Mg
