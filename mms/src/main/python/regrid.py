__author__ = 'Ralf Quast'

from regridworkflow import RegridWorkflow

usecase = "regrid"
archive_root = "/group_workspaces/cems2/esacci_sst/output/v2.1.10"
target_root = "/group_workspaces/cems2/esacci_sst/scratch/2015_10_regridded_sst"

w = RegridWorkflow(usecase, archive_root, target_root)
w.add_sensor("all_sensors", "1991-08-01", "2015-05-31")
w.run()
