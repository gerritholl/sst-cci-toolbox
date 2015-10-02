from workflow import Period
from workflow import Workflow

usecase = 'mms11b'
mmdtype = 'mmd11'

w = Workflow(usecase)
w.add_primary_sensor('avhrr.n10', '1986-11-17', '1991-09-17')
w.add_primary_sensor('avhrr.n11', '1988-11-08', '1994-09-14')
#w.add_primary_sensor('avhrr.n12', '1991-09-16', '1998-12-15')
#w.add_primary_sensor('avhrr.n14', '1995-01-01', '2000-01-01')
#w.add_primary_sensor('avhrr.n15', '1998-10-26', '2003-04-09')
#w.add_primary_sensor('avhrr.n15', '2003-12-21', '2011-01-01')
#w.add_primary_sensor('avhrr.n16', '2001-01-01', '2003-04-09')
#w.add_primary_sensor('avhrr.n16', '2003-12-21', '2011-01-01')
#w.add_primary_sensor('avhrr.n17', '2002-07-10', '2003-04-09')
#w.add_primary_sensor('avhrr.n17', '2003-12-21', '2011-01-01')
#w.add_primary_sensor('avhrr.n18', '2005-06-05', '2014-01-01')
#w.add_primary_sensor('avhrr.n19', '2009-02-06', '2014-01-01')
#w.add_primary_sensor('avhrr.m02', '2006-11-21', '2014-01-01')
w.add_secondary_sensor('avhrr.n10', '1986-11-17', '1991-09-17')
w.add_secondary_sensor('avhrr.n11', '1988-11-08', '1994-09-14')
#w.add_secondary_sensor('avhrr.n12', '1991-09-16', '1998-12-15')
#w.add_secondary_sensor('avhrr.n14', '1995-01-01', '2000-01-01')
#w.add_secondary_sensor('avhrr.n15', '1998-10-26', '2003-04-09')
#w.add_secondary_sensor('avhrr.n15', '2003-12-21', '2011-01-01')
#w.add_secondary_sensor('avhrr.n16', '2001-01-01', '2003-04-09')
#w.add_secondary_sensor('avhrr.n16', '2003-12-21', '2011-01-01')
#w.add_secondary_sensor('avhrr.n17', '2002-07-10', '2003-04-09')
#w.add_secondary_sensor('avhrr.n17', '2003-12-21', '2010-10-01')
#w.add_secondary_sensor('avhrr.n18', '2005-06-05', '2014-01-01')
#w.add_secondary_sensor('avhrr.n19', '2009-02-06', '2014-01-01')
#w.add_secondary_sensor('avhrr.m02', '2006-11-21', '2014-01-01')
w.set_samples_per_month(80000000)

w.run(mmdtype, hosts=[('localhost', 48)], selected_only=True)
