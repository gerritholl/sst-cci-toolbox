__author__ = 'Ralf Quast'

import os
import re
from netCDF4 import Dataset
from netCDF4 import Variable
import json

import numpy
import numpy.ma as ma


class VerificationError(Exception):
    exit_code = 2


class ProductType:
    def __init__(self, variable_names, geophysical_check_spec, mask_consistency_check_specs, sst_variable_names):
        """

        :type variable_names: list
        :type geophysical_check_spec: list
        :type mask_consistency_check_specs: list
        :type sst_variable_names: list
        """
        self.variable_names = variable_names
        self.geophysical_check_spec = geophysical_check_spec
        self.mask_consistency_check_specs = mask_consistency_check_specs
        self.sst_variable_names = sst_variable_names

    def get_variable_names(self):
        """

        :rtype : list
        """
        return self.variable_names

    def get_geophysical_check_spec(self):
        """

        :rtype : list
        """
        return self.geophysical_check_spec

    def get_mask_consistency_check_specs(self):
        """

        :rtype : list
        """
        return self.mask_consistency_check_specs

    def get_sst_variable_names(self):
        """

        :rtype : list
        """
        return self.sst_variable_names


class L2P(ProductType):
    def __init__(self):
        ProductType.__init__(self,
                             [
                                 'lat',
                                 'lon',
                                 'time',
                                 'sst_dtime',
                                 'sea_surface_temperature',
                                 'sea_surface_temperature_depth',
                                 'wind_speed',
                                 'quality_level',
                                 'sses_standard_deviation',
                                 'sst_depth_total_uncertainty',
                                 'large_scale_correlated_uncertainty',
                                 'synoptically_correlated_uncertainty',
                                 'uncorrelated_uncertainty',
                                 'adjustment_uncertainty',
                                 'l2p_flags',
                                 'sses_bias'
                             ],
                             ['sea_surface_temperature', 'sea_surface_temperature_depth', -5.0, 10.0],
                             [
                                 ['sea_surface_temperature', 'sses_bias'],
                                 ['sea_surface_temperature', 'sses_standard_deviation'],
                                 ['sea_surface_temperature', 'large_scale_correlated_uncertainty'],
                                 ['sea_surface_temperature', 'synoptically_correlated_uncertainty'],
                                 ['sea_surface_temperature', 'uncorrelated_uncertainty'],
                                 ['sea_surface_temperature', 'adjustment_uncertainty'],
                                 ['sea_surface_temperature_depth', 'sst_depth_total_uncertainty']
                             ],
                             [
                                 'sea_surface_temperature',
                                 'sea_surface_temperature_depth'
                             ])


class L3U(ProductType):
    def __init__(self):
        ProductType.__init__(self,
                             [
                                 'lat',
                                 'lat_bnds',
                                 'lon',
                                 'lon_bnds',
                                 'time',
                                 'time_bnds',
                                 'sst_dtime',
                                 'sea_surface_temperature',
                                 'sea_surface_temperature_depth',
                                 'wind_speed',
                                 'quality_level',
                                 'sses_standard_deviation',
                                 'sst_depth_total_uncertainty',
                                 'large_scale_correlated_uncertainty',
                                 'synoptically_correlated_uncertainty',
                                 'uncorrelated_uncertainty',
                                 'adjustment_uncertainty',
                                 'l2p_flags',
                                 'sses_bias'
                             ],
                             ['sea_surface_temperature', 'sea_surface_temperature_depth', -5.0, 10.0],
                             [
                                 ['sea_surface_temperature', 'sses_bias'],
                                 ['sea_surface_temperature', 'sses_standard_deviation'],
                                 ['sea_surface_temperature', 'large_scale_correlated_uncertainty'],
                                 ['sea_surface_temperature', 'synoptically_correlated_uncertainty'],
                                 ['sea_surface_temperature', 'uncorrelated_uncertainty'],
                                 ['sea_surface_temperature', 'adjustment_uncertainty'],
                                 ['sea_surface_temperature_depth', 'sst_depth_total_uncertainty']
                             ],
                             [
                                 'sea_surface_temperature',
                                 'sea_surface_temperature_depth'
                             ])


class L4(ProductType):
    def __init__(self):
        ProductType.__init__(self,
                             [
                                 'lat',
                                 'lat_bnds',
                                 'lon',
                                 'lon_bnds',
                                 'time',
                                 'time_bnds',
                                 'analysed_sst',
                                 'sea_ice_fraction',
                                 'quality_level'
                                 'analysis_error',
                                 'sea_ice_fraction_error',
                                 'mask',
                             ], [],
                             [
                                 ['analysed_sst', 'analysis_error'],
                                 ['sea_ice_fraction', 'sea_ice_fraction_error']
                             ],
                             [
                                 'analysed_sst'
                             ])


class ProductVerifier:
    def __init__(self, source_pathname, report_pathname=None):
        """

        :type source_pathname: str
        :type report_pathname: str
        """
        self.source_pathname = source_pathname
        self.report_pathname = report_pathname
        self.report = {}
        self.filename_patterns = {
            '[0-9]{14}-ESACCI-L2P_GHRSST-.*\\.nc': L2P(),
        }

    def get_source_pathname(self):
        """

        :rtype : str
        """
        return self.source_pathname

    def get_report_pathname(self):
        """

        :rtype : str
        """
        return self.report_pathname

    def get_report(self):
        return self.report

    def verify(self):
        try:
            self._check_source_pathname()
            product_type = self._check_source_filename()
            dataset = self._check_product_can_be_opened()
            self._check_dataset(dataset, product_type)
        except VerificationError:
            self.report['verification_error'] = self.get_source_pathname()
            print 'VerificationError:', self.get_source_pathname()
        finally:
            ProductVerifier.dump_report(self.report, self.report_pathname)

    def _check_source_pathname(self):
        ok = os.path.isfile(self.source_pathname)
        if ok:
            self.report['source_pathname_check'] = 0
        else:
            self.report['source_pathname_check'] = 1
            raise VerificationError

    def _check_source_filename(self):
        """

        :rtype : ProductType
        """
        product_type = None

        filename = os.path.basename(self.source_pathname)
        for p, t in self.filename_patterns.iteritems():
            if re.match(p, filename):
                product_type = t
                break

        if product_type is not None:
            self.report['source_filename_check'] = 0
            return product_type
        else:
            self.report['source_filename_check'] = 1
            raise VerificationError

    def _check_variable_existence(self, dataset, product_type):
        """

        :type dataset: Dataset
        :type product_type: ProductType
        """
        for variable_name in product_type.get_variable_names():
            if variable_name in dataset.variables:
                self.report[variable_name + '.existence_check'] = 0
            else:
                self.report[variable_name + '.existence_check'] = 1

    @staticmethod
    def __get_masked_data(variable):
        """

        :type variable: Variable
        :rtype : ma.MaskedArray
        """
        data = variable[:]
        try:
            fill_value = variable.getncattr('_FillValue')
            return ma.masked_equal(data, fill_value)
        except AttributeError:
            return ma.array(data)

    @staticmethod
    def __get_data(dataset, variable_name):
        """

        :type dataset: Dataset
        :type variable_name: str
        :rtype : ma.MaskedArray
        """
        return ProductVerifier.__get_masked_data(dataset.variables[variable_name])

    def _check_variable_limits(self, dataset):
        """

        :type dataset: Dataset
        """
        for variable_name in dataset.variables:
            variable = dataset.variables[variable_name]
            self.report[variable_name + '.count.total'] = variable.size

            data = ProductVerifier.__get_masked_data(variable)
            self.report[variable_name + '.count.valid'] = data.count()

            try:
                valid_max = variable.getncattr('valid_max')
                invalid_data = ma.masked_less_equal(data, valid_max)
                self.report[variable_name + '.valid_max_check'] = invalid_data.count()
            except AttributeError:
                pass
            try:
                valid_min = variable.getncattr('valid_min')
                invalid_data = ma.masked_greater_equal(data, valid_min)
                self.report[variable_name + '.valid_min_check'] = invalid_data.count()
            except AttributeError:
                pass

    def _check_geophysical(self, dataset, product_type):
        """

        :type dataset: Dataset
        :type product_type: ProductType
        """
        spec = product_type.get_geophysical_check_spec()
        if len(spec) != 0:
            a = ProductVerifier.__get_data(dataset, spec[0])
            b = ProductVerifier.__get_data(dataset, spec[1])
            difference_data = a - b
            suspicious_data = ma.masked_inside(difference_data, spec[2], spec[3])
            self.report['geophysical_check'] = suspicious_data.count()

    # noinspection PyNoneFunctionAssignment,PyUnresolvedReferences
    def _check_mask_consistency(self, dataset, product_type):
        """

        :type dataset: Dataset
        :type product_type: ProductType
        """
        for spec in product_type.get_mask_consistency_check_specs():
            reference_variable_name = spec[0]
            objective_variable_name = spec[1]
            a = ma.getmaskarray(ProductVerifier.__get_data(dataset, reference_variable_name))
            b = ma.getmaskarray(ProductVerifier.__get_data(dataset, objective_variable_name))
            # false negatives: element is not masked in a, but masked in b
            false_negatives = ma.masked_equal(numpy.logical_or(numpy.logical_not(a), b), True)
            self.report[objective_variable_name + '.mask_false_negative_check'] = false_negatives.count()
            # false positives: element is masked in a, but not masked in b
            false_positives = ma.masked_equal(numpy.logical_or(numpy.logical_not(b), a), True)
            self.report[objective_variable_name + '.mask_false_positive_check'] = false_positives.count()

    def _check_product_can_be_opened(self):
        try:
            dataset = Dataset(self.source_pathname)
            self.report['product_can_be_opened_check'] = 0
        except:
            self.report['product_can_be_opened_check'] = 1
            raise VerificationError
        return dataset

    def _check_dataset(self, dataset, product_type):
        """

        :type dataset: Dataset
        :type product_type: ProductType
        """
        try:
            self._check_variable_existence(dataset, product_type)
            self._check_variable_limits(dataset)
            self._check_geophysical(dataset, product_type)
            self._check_mask_consistency(dataset, product_type)
            self._check_corruptness(dataset, product_type)
        finally:
            try:
                dataset.close()
            except IOError:
                pass

    @staticmethod
    def load_report(report_pathname):
        """

        :type report_pathname: str
        :rtype : dict
        """
        report_file = open(report_pathname, 'r')
        try:
            return json.load(report_file)
        finally:
            report_file.close()

    @staticmethod
    def dump_report(report, report_pathname=None):
        """

        :type report: dict
        :type report_pathname: str
        """
        if report_pathname is None:
            print json.dumps(report, indent=1)
        else:
            report_file = open(report_pathname, 'w')
            try:
                json.dump(report, report_file, indent=1)
            finally:
                report_file.close()

    def _check_corruptness(self, dataset, product_type):
        """

        :type dataset: Dataset
        :type product_type: ProductType
        """
        ok = True
        for variable_name in product_type.get_sst_variable_names():
            if variable_name in dataset.variables:
                variable = dataset.variables[variable_name]

                data = ProductVerifier.__get_masked_data(variable)
                valid_data_count = data.count()
                if valid_data_count == 0:
                    ok = False
                try:
                    valid_max = variable.getncattr('valid_max')
                    invalid_data = ma.masked_less_equal(data, valid_max)
                    valid_data_count = valid_data_count - invalid_data.count()
                except AttributeError:
                    pass
                try:
                    valid_min = variable.getncattr('valid_min')
                    invalid_data = ma.masked_greater_equal(data, valid_min)
                    valid_data_count = valid_data_count - invalid_data.count()
                except AttributeError:
                    pass
                if valid_data_count == 0:
                    ok = False
            else:
                ok = False
        if ok:
            self.report['corruptness_check'] = 0
        else:
            self.report['corruptness_check'] = 1
            raise VerificationError


if __name__ == "__main__":
    # Call with one or two arguments:
    #
    # 1 = source pathname
    # 2 = report pathname (optional)
    import sys

    argument_count = len(sys.argv)
    if argument_count == 2:
        verifier = ProductVerifier(sys.argv[1])
    elif argument_count == 3:
        verifier = ProductVerifier(sys.argv[1], sys.argv[2])
    else:
        print 'usage:', sys.argv[0], '<source pathname> <report pathname>'
        sys.exit(1)

    # noinspection PyBroadException
    try:
        verifier.verify()
        sys.exit()
    except:
        sys.exit(1)