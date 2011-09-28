/*
 * Copyright (C) 2011 Brockmann Consult GmbH (info@brockmann-consult.de)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see http://www.gnu.org/licenses/
 */

package org.esa.cci.sst.tools;

/**
* Provides missing variables for {@link TargetVariableConfigurationTest}.
*
* @author Thomas Storm
*/
class VarsHolder {

static final String[] VARIABLES = new String[]{
        "arc3.atsr.1.sea_surface_temperature.op.N2",
        "arc3.atsr.1.sea_surface_temperature.op.N3",
        "arc3.atsr.1.sea_surface_temperature.op.D2",
        "arc3.atsr.1.sea_surface_temperature.op.D3",
        "arc3.atsr.1.sea_surface_temperature.ARC.N2",
        "arc3.atsr.1.sea_surface_temperature.ARC.N3",
        "arc3.atsr.1.sea_surface_temperature.ARC.D2",
        "arc3.atsr.1.sea_surface_temperature.ARC.D3",
        "arc3.atsr.1.sea_surface_temperature.ARC.D3",
        "arc3.atsr.1.sea_surface_temperature.ARC.D3",
        "arc3.atsr.1.cloud_mask.bayes_nadir_min    ",
        "arc3.atsr.1.cloud_mask.bayes_nadir_max                ",
        "arc3.atsr.1.cloud_mask.bayes_forward_min              ",
        "arc3.atsr.1.cloud_mask.bayes_forward_max              ",
        "arc3.atsr.1.cloud_mask.bayes_dual_min                 ",
        "arc3.atsr.1.cloud_mask.bayes_dual_max                 ",
        "arc3.atsr.1.cloud_mask.spec_nadir_min                 ",
        "arc3.atsr.1.cloud_mask.spec_nadir_max                 ",
        "arc3.atsr.1.cloud_mask.spec_forward_min               ",
        "arc3.atsr.1.cloud_mask.spec_forward_max               ",
        "arc3.atsr.1.cloud_mask.spec_dual_min                  ",
        "arc3.atsr.1.cloud_mask.spec_dual_max                  ",
        "arc3.atsr.1.saharan_dust_index.2",
        "arc3.atsr.1.saharan_dust_index.3",
        "arc3.atsr.2.sea_surface_temperature.op.N2",
        "arc3.atsr.2.sea_surface_temperature.op.N3",
        "arc3.atsr.2.sea_surface_temperature.op.D2",
        "arc3.atsr.2.sea_surface_temperature.op.D3",
        "arc3.atsr.2.sea_surface_temperature.ARC.N2",
        "arc3.atsr.2.sea_surface_temperature.ARC.N3",
        "arc3.atsr.2.sea_surface_temperature.ARC.D2",
        "arc3.atsr.2.sea_surface_temperature.ARC.D3",
        "arc3.atsr.2.sea_surface_temperature.ARC.D3",
        "arc3.atsr.2.sea_surface_temperature.ARC.D3",
        "arc3.atsr.2.cloud_mask.bayes_nadir_min    ",
        "arc3.atsr.2.cloud_mask.bayes_nadir_max                ",
        "arc3.atsr.2.cloud_mask.bayes_forward_min              ",
        "arc3.atsr.2.cloud_mask.bayes_forward_max              ",
        "arc3.atsr.2.cloud_mask.bayes_dual_min                 ",
        "arc3.atsr.2.cloud_mask.bayes_dual_max                 ",
        "arc3.atsr.2.cloud_mask.spec_nadir_min                 ",
        "arc3.atsr.2.cloud_mask.spec_nadir_max                 ",
        "arc3.atsr.2.cloud_mask.spec_forward_min               ",
        "arc3.atsr.2.cloud_mask.spec_forward_max               ",
        "arc3.atsr.2.cloud_mask.spec_dual_min                  ",
        "arc3.atsr.2.cloud_mask.spec_dual_max                  ",
        "arc3.atsr.2.saharan_dust_index.2",
        "arc3.atsr.2.saharan_dust_index.3",
        "arc3.atsr.3.sea_surface_temperature.op.N2",
        "arc3.atsr.3.sea_surface_temperature.op.N3",
        "arc3.atsr.3.sea_surface_temperature.op.D2",
        "arc3.atsr.3.sea_surface_temperature.op.D3",
        "arc3.atsr.3.sea_surface_temperature.ARC.N2",
        "arc3.atsr.3.sea_surface_temperature.ARC.N3",
        "arc3.atsr.3.sea_surface_temperature.ARC.D2",
        "arc3.atsr.3.sea_surface_temperature.ARC.D3",
        "arc3.atsr.3.sea_surface_temperature.ARC.D3",
        "arc3.atsr.3.sea_surface_temperature.ARC.D3",
        "arc3.atsr.3.cloud_mask.bayes_nadir_min    ",
        "arc3.atsr.3.cloud_mask.bayes_nadir_max                ",
        "arc3.atsr.3.cloud_mask.bayes_forward_min              ",
        "arc3.atsr.3.cloud_mask.bayes_forward_max              ",
        "arc3.atsr.3.cloud_mask.bayes_dual_min                 ",
        "arc3.atsr.3.cloud_mask.bayes_dual_max                 ",
        "arc3.atsr.3.cloud_mask.spec_nadir_min                 ",
        "arc3.atsr.3.cloud_mask.spec_nadir_max                 ",
        "arc3.atsr.3.cloud_mask.spec_forward_min               ",
        "arc3.atsr.3.cloud_mask.spec_forward_max               ",
        "arc3.atsr.3.cloud_mask.spec_dual_min                  ",
        "arc3.atsr.3.cloud_mask.spec_dual_max                  ",
        "arc3.atsr.3.saharan_dust_index.2",
        "arc3.atsr.3.saharan_dust_index.3",
        "avhrr.TN.latitude",
        "avhrr.TN.longitude           ",
        "avhrr.TN.reflec_1            ",
        "avhrr.TN.reflec_2            ",
        "avhrr.TN.reflec_3a           ",
        "avhrr.TN.temp_3b             ",
        "avhrr.TN.temp_4              ",
        "avhrr.TN.temp_5              ",
        "avhrr.TN.sun_zenith          ",
        "avhrr.TN.view_zenith         ",
        "avhrr.TN.delta_azimuth       ",
        "avhrr.TN.ict_temp            ",
        "avhrr.TN.reflec_to_rad_1     ",
        "avhrr.TN.reflec_to_rad_2     ",
        "avhrr.TN.reflec_to_rad_3a    ",
        "avhrr.TN.bad_data            ",
        "avhrr.TN.cloud_flag          ",
        "avhrr.TN.cloud_prob          ",
        "avhrr.TN.l1b_filename        ",
        "avhrr.TN.matchup_elem        ",
        "avhrr.TN.matchup_line        ",
        "avhrr.6.latitude",
        "avhrr.6.longitude           ",
        "avhrr.6.reflec_1            ",
        "avhrr.6.reflec_2            ",
        "avhrr.6.reflec_3a           ",
        "avhrr.6.temp_3b             ",
        "avhrr.6.temp_4              ",
        "avhrr.6.temp_5              ",
        "avhrr.6.sun_zenith          ",
        "avhrr.6.view_zenith         ",
        "avhrr.6.delta_azimuth       ",
        "avhrr.6.ict_temp            ",
        "avhrr.6.reflec_to_rad_1     ",
        "avhrr.6.reflec_to_rad_2     ",
        "avhrr.6.reflec_to_rad_3a    ",
        "avhrr.6.bad_data            ",
        "avhrr.6.cloud_flag          ",
        "avhrr.6.cloud_prob          ",
        "avhrr.6.l1b_filename        ",
        "avhrr.6.matchup_elem        ",
        "avhrr.6.matchup_line        ",
        "avhrr.7.latitude",
        "avhrr.7.longitude           ",
        "avhrr.7.reflec_1            ",
        "avhrr.7.reflec_2            ",
        "avhrr.7.reflec_3a           ",
        "avhrr.7.temp_3b             ",
        "avhrr.7.temp_4              ",
        "avhrr.7.temp_5              ",
        "avhrr.7.sun_zenith          ",
        "avhrr.7.view_zenith         ",
        "avhrr.7.delta_azimuth       ",
        "avhrr.7.ict_temp            ",
        "avhrr.7.reflec_to_rad_1     ",
        "avhrr.7.reflec_to_rad_2     ",
        "avhrr.7.reflec_to_rad_3a    ",
        "avhrr.7.bad_data            ",
        "avhrr.7.cloud_flag          ",
        "avhrr.7.cloud_prob          ",
        "avhrr.7.l1b_filename        ",
        "avhrr.7.matchup_elem        ",
        "avhrr.7.matchup_line        ",
        "avhrr.8.latitude",
        "avhrr.8.longitude           ",
        "avhrr.8.reflec_1            ",
        "avhrr.8.reflec_2            ",
        "avhrr.8.reflec_3a           ",
        "avhrr.8.temp_3b             ",
        "avhrr.8.temp_4              ",
        "avhrr.8.temp_5              ",
        "avhrr.8.sun_zenith          ",
        "avhrr.8.view_zenith         ",
        "avhrr.8.delta_azimuth       ",
        "avhrr.8.ict_temp            ",
        "avhrr.8.reflec_to_rad_1     ",
        "avhrr.8.reflec_to_rad_2     ",
        "avhrr.8.reflec_to_rad_3a    ",
        "avhrr.8.bad_data            ",
        "avhrr.8.cloud_flag          ",
        "avhrr.8.cloud_prob          ",
        "avhrr.8.l1b_filename        ",
        "avhrr.8.matchup_elem        ",
        "avhrr.8.matchup_line        ",
        "avhrr.9.latitude",
        "avhrr.9.longitude           ",
        "avhrr.9.reflec_1            ",
        "avhrr.9.reflec_2            ",
        "avhrr.9.reflec_3a           ",
        "avhrr.9.temp_3b             ",
        "avhrr.9.temp_4              ",
        "avhrr.9.temp_5              ",
        "avhrr.9.sun_zenith          ",
        "avhrr.9.view_zenith         ",
        "avhrr.9.delta_azimuth       ",
        "avhrr.9.ict_temp            ",
        "avhrr.9.reflec_to_rad_1     ",
        "avhrr.9.reflec_to_rad_2     ",
        "avhrr.9.reflec_to_rad_3a    ",
        "avhrr.9.bad_data            ",
        "avhrr.9.cloud_flag          ",
        "avhrr.9.cloud_prob          ",
        "avhrr.9.l1b_filename        ",
        "avhrr.9.matchup_elem        ",
        "avhrr.9.matchup_line        ",
        "avhrr.10.latitude",
        "avhrr.10.longitude           ",
        "avhrr.10.reflec_1            ",
        "avhrr.10.reflec_2            ",
        "avhrr.10.reflec_3a           ",
        "avhrr.10.temp_3b             ",
        "avhrr.10.temp_4              ",
        "avhrr.10.temp_5              ",
        "avhrr.10.sun_zenith          ",
        "avhrr.10.view_zenith         ",
        "avhrr.10.delta_azimuth       ",
        "avhrr.10.ict_temp            ",
        "avhrr.10.reflec_to_rad_1     ",
        "avhrr.10.reflec_to_rad_2     ",
        "avhrr.10.reflec_to_rad_3a    ",
        "avhrr.10.bad_data            ",
        "avhrr.10.cloud_flag          ",
        "avhrr.10.cloud_prob          ",
        "avhrr.10.l1b_filename        ",
        "avhrr.10.matchup_elem        ",
        "avhrr.10.matchup_line        ",
        "avhrr.11.latitude",
        "avhrr.11.longitude           ",
        "avhrr.11.reflec_1            ",
        "avhrr.11.reflec_2            ",
        "avhrr.11.reflec_3a           ",
        "avhrr.11.temp_3b             ",
        "avhrr.11.temp_4              ",
        "avhrr.11.temp_5              ",
        "avhrr.11.sun_zenith          ",
        "avhrr.11.view_zenith         ",
        "avhrr.11.delta_azimuth       ",
        "avhrr.11.ict_temp            ",
        "avhrr.11.reflec_to_rad_1     ",
        "avhrr.11.reflec_to_rad_2     ",
        "avhrr.11.reflec_to_rad_3a    ",
        "avhrr.11.bad_data            ",
        "avhrr.11.cloud_flag          ",
        "avhrr.11.cloud_prob          ",
        "avhrr.11.l1b_filename        ",
        "avhrr.11.matchup_elem        ",
        "avhrr.11.matchup_line        ",
        "avhrr.12.latitude",
        "avhrr.12.longitude           ",
        "avhrr.12.reflec_1            ",
        "avhrr.12.reflec_2            ",
        "avhrr.12.reflec_3a           ",
        "avhrr.12.temp_3b             ",
        "avhrr.12.temp_4              ",
        "avhrr.12.temp_5              ",
        "avhrr.12.sun_zenith          ",
        "avhrr.12.view_zenith         ",
        "avhrr.12.delta_azimuth       ",
        "avhrr.12.ict_temp            ",
        "avhrr.12.reflec_to_rad_1     ",
        "avhrr.12.reflec_to_rad_2     ",
        "avhrr.12.reflec_to_rad_3a    ",
        "avhrr.12.bad_data            ",
        "avhrr.12.cloud_flag          ",
        "avhrr.12.cloud_prob          ",
        "avhrr.12.l1b_filename        ",
        "avhrr.12.matchup_elem        ",
        "avhrr.12.matchup_line        ",
        "avhrr.13.latitude",
        "avhrr.13.longitude           ",
        "avhrr.13.reflec_1            ",
        "avhrr.13.reflec_2            ",
        "avhrr.13.reflec_3a           ",
        "avhrr.13.temp_3b             ",
        "avhrr.13.temp_4              ",
        "avhrr.13.temp_5              ",
        "avhrr.13.sun_zenith          ",
        "avhrr.13.view_zenith         ",
        "avhrr.13.delta_azimuth       ",
        "avhrr.13.ict_temp            ",
        "avhrr.13.reflec_to_rad_1     ",
        "avhrr.13.reflec_to_rad_2     ",
        "avhrr.13.reflec_to_rad_3a    ",
        "avhrr.13.bad_data            ",
        "avhrr.13.cloud_flag          ",
        "avhrr.13.cloud_prob          ",
        "avhrr.13.l1b_filename        ",
        "avhrr.13.matchup_elem        ",
        "avhrr.13.matchup_line        ",
        "avhrr.14.latitude",
        "avhrr.14.longitude           ",
        "avhrr.14.reflec_1            ",
        "avhrr.14.reflec_2            ",
        "avhrr.14.reflec_3a           ",
        "avhrr.14.temp_3b             ",
        "avhrr.14.temp_4              ",
        "avhrr.14.temp_5              ",
        "avhrr.14.sun_zenith          ",
        "avhrr.14.view_zenith         ",
        "avhrr.14.delta_azimuth       ",
        "avhrr.14.ict_temp            ",
        "avhrr.14.reflec_to_rad_1     ",
        "avhrr.14.reflec_to_rad_2     ",
        "avhrr.14.reflec_to_rad_3a    ",
        "avhrr.14.bad_data            ",
        "avhrr.14.cloud_flag          ",
        "avhrr.14.cloud_prob          ",
        "avhrr.14.l1b_filename        ",
        "avhrr.14.matchup_elem        ",
        "avhrr.14.matchup_line        ",
        "avhrr.15.latitude",
        "avhrr.15.longitude           ",
        "avhrr.15.reflec_1            ",
        "avhrr.15.reflec_2            ",
        "avhrr.15.reflec_3a           ",
        "avhrr.15.temp_3b             ",
        "avhrr.15.temp_4              ",
        "avhrr.15.temp_5              ",
        "avhrr.15.sun_zenith          ",
        "avhrr.15.view_zenith         ",
        "avhrr.15.delta_azimuth       ",
        "avhrr.15.ict_temp            ",
        "avhrr.15.reflec_to_rad_1     ",
        "avhrr.15.reflec_to_rad_2     ",
        "avhrr.15.reflec_to_rad_3a    ",
        "avhrr.15.bad_data            ",
        "avhrr.15.cloud_flag          ",
        "avhrr.15.cloud_prob          ",
        "avhrr.15.l1b_filename        ",
        "avhrr.15.matchup_elem        ",
        "avhrr.15.matchup_line        ",
        "avhrr.16.latitude",
        "avhrr.16.longitude           ",
        "avhrr.16.reflec_1            ",
        "avhrr.16.reflec_2            ",
        "avhrr.16.reflec_3a           ",
        "avhrr.16.temp_3b             ",
        "avhrr.16.temp_4              ",
        "avhrr.16.temp_5              ",
        "avhrr.16.sun_zenith          ",
        "avhrr.16.view_zenith         ",
        "avhrr.16.delta_azimuth       ",
        "avhrr.16.ict_temp            ",
        "avhrr.16.reflec_to_rad_1     ",
        "avhrr.16.reflec_to_rad_2     ",
        "avhrr.16.reflec_to_rad_3a    ",
        "avhrr.16.bad_data            ",
        "avhrr.16.cloud_flag          ",
        "avhrr.16.cloud_prob          ",
        "avhrr.16.l1b_filename        ",
        "avhrr.16.matchup_elem        ",
        "avhrr.16.matchup_line        ",
        "avhrr.17.latitude",
        "avhrr.17.longitude           ",
        "avhrr.17.reflec_1            ",
        "avhrr.17.reflec_2            ",
        "avhrr.17.reflec_3a           ",
        "avhrr.17.temp_3b             ",
        "avhrr.17.temp_4              ",
        "avhrr.17.temp_5              ",
        "avhrr.17.sun_zenith          ",
        "avhrr.17.view_zenith         ",
        "avhrr.17.delta_azimuth       ",
        "avhrr.17.ict_temp            ",
        "avhrr.17.reflec_to_rad_1     ",
        "avhrr.17.reflec_to_rad_2     ",
        "avhrr.17.reflec_to_rad_3a    ",
        "avhrr.17.bad_data            ",
        "avhrr.17.cloud_flag          ",
        "avhrr.17.cloud_prob          ",
        "avhrr.17.l1b_filename        ",
        "avhrr.17.matchup_elem        ",
        "avhrr.17.matchup_line        ",
        "avhrr.18.latitude",
        "avhrr.18.longitude           ",
        "avhrr.18.reflec_1            ",
        "avhrr.18.reflec_2            ",
        "avhrr.18.reflec_3a           ",
        "avhrr.18.temp_3b             ",
        "avhrr.18.temp_4              ",
        "avhrr.18.temp_5              ",
        "avhrr.18.sun_zenith          ",
        "avhrr.18.view_zenith         ",
        "avhrr.18.delta_azimuth       ",
        "avhrr.18.ict_temp            ",
        "avhrr.18.reflec_to_rad_1     ",
        "avhrr.18.reflec_to_rad_2     ",
        "avhrr.18.reflec_to_rad_3a    ",
        "avhrr.18.bad_data            ",
        "avhrr.18.cloud_flag          ",
        "avhrr.18.cloud_prob          ",
        "avhrr.18.l1b_filename        ",
        "avhrr.18.matchup_elem        ",
        "avhrr.18.matchup_line        ",
        "avhrr.19.latitude",
        "avhrr.19.longitude           ",
        "avhrr.19.reflec_1            ",
        "avhrr.19.reflec_2            ",
        "avhrr.19.reflec_3a           ",
        "avhrr.19.temp_3b             ",
        "avhrr.19.temp_4              ",
        "avhrr.19.temp_5              ",
        "avhrr.19.sun_zenith          ",
        "avhrr.19.view_zenith         ",
        "avhrr.19.delta_azimuth       ",
        "avhrr.19.ict_temp            ",
        "avhrr.19.reflec_to_rad_1     ",
        "avhrr.19.reflec_to_rad_2     ",
        "avhrr.19.reflec_to_rad_3a    ",
        "avhrr.19.bad_data            ",
        "avhrr.19.cloud_flag          ",
        "avhrr.19.cloud_prob          ",
        "avhrr.19.l1b_filename        ",
        "avhrr.19.matchup_elem        ",
        "avhrr.19.matchup_line        ",
        "avhrr.M2.latitude",
        "avhrr.M2.longitude           ",
        "avhrr.M2.reflec_1            ",
        "avhrr.M2.reflec_2            ",
        "avhrr.M2.reflec_3a           ",
        "avhrr.M2.temp_3b             ",
        "avhrr.M2.temp_4              ",
        "avhrr.M2.temp_5              ",
        "avhrr.M2.sun_zenith          ",
        "avhrr.M2.view_zenith         ",
        "avhrr.M2.delta_azimuth       ",
        "avhrr.M2.ict_temp            ",
        "avhrr.M2.reflec_to_rad_1     ",
        "avhrr.M2.reflec_to_rad_2     ",
        "avhrr.M2.reflec_to_rad_3a    ",
        "avhrr.M2.bad_data            ",
        "avhrr.M2.cloud_flag          ",
        "avhrr.M2.cloud_prob          ",
        "avhrr.M2.l1b_filename        ",
        "avhrr.M2.matchup_elem        ",
        "avhrr.M2.matchup_line        ",
        "seaice.lat",
        "seaice.lon",
        "seaice.sea_ice_concentration"
};

    private VarsHolder() {
    }
}