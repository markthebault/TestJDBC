load data
  infile './Bons_Depart.txt' 
  into table BONS_DEPART
  replace
  fields terminated by "#"
 (numbon, rep1, rep2)
