package com.niaw.launchnea

import assertk.assertThat

import com.niaw.launchnea.database.entities.Launch

import com.niaw.launchnea.networking.parseLaunch

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import java.time.ZonedDateTime
import kotlin.test.Test

class ParseLaunchTest {

    @Test
    fun testNoReuse() {
        val jsonString = """{
            "id": "17d0ec68-f466-4916-9dc5-16123eda6484",
            "url": "https://lldev.thespacedevs.com/2.3.0/launches/17d0ec68-f466-4916-9dc5-16123eda6484/",
            "name": "GSLV Mk II | GISAT-1A (EOS-05)",
            "response_mode": "detailed",
            "slug": "gslv-mk-ii-gisat-1a-eos-05",
            "launch_designator": null,
            "status": {
            "id": 2,
            "name": "To Be Determined",
            "abbrev": "TBD",
            "description": "Current date is a placeholder or rough estimation based on unreliable or interpreted sources."
        },
            "last_updated": "2025-09-14T23:55:30Z",
            "net": "2026-03-31T00:00:00Z",
            "net_precision": {
            "id": 8,
            "name": "Quarter 1",
            "abbrev": "Q1",
            "description": "The T-0 is expected in the first quarter of the given year."
        },
            "window_end": "2026-03-31T00:00:00Z",
            "window_start": "2026-03-31T00:00:00Z",
            "image": {
            "id": 1744,
            "name": "[AUTO] GSLV Mk II - image",
            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/gslv2520mk2520ii_image_20190825171642.jpg",
            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305193640.jpeg",
            "credit": null,
            "license": {
            "id": 1,
            "name": "Unknown",
            "priority": 9,
            "link": null
        },
            "single_use": true,
            "variants": []
        },
            "infographic": null,
            "probability": null,
            "weather_concerns": null,
            "failreason": "",
            "hashtag": null,
            "launch_service_provider": {
            "response_mode": "normal",
            "id": 31,
            "url": "https://lldev.thespacedevs.com/2.3.0/agencies/31/",
            "name": "Indian Space Research Organization",
            "abbrev": "ISRO",
            "type": {
            "id": 1,
            "name": "Government"
        },
            "featured": true,
            "country": [
            {
                "id": 9,
                "name": "India",
                "alpha_2_code": "IN",
                "alpha_3_code": "IND",
                "nationality_name": "Indian",
                "nationality_name_composed": "Indo"
            }
            ],
            "description": "The Indian Space Research Organisation (ISRO) is the space agency of the Government of India headquartered in the city of Bangalore. Its vision is to \"harness space technology for national development while pursuing space science research and planetary exploration.\"",
            "administrator": "Chairman: V. Narayanan",
            "founding_year": 1969,
            "launchers": "PSLV | GSLV",
            "spacecraft": "Gaganyaan",
            "parent": null,
            "image": {
            "id": 14,
            "name": "[AUTO] Indian Space Research Organization - image",
            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/indian2520space2520research2520organization_image_20190215225409.jpeg",
            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184615.jpeg",
            "credit": null,
            "license": {
            "id": 1,
            "name": "Unknown",
            "priority": 9,
            "link": null
        },
            "single_use": true,
            "variants": []
        },
            "logo": {
            "id": 141,
            "name": "[AUTO] Indian Space Research Organization - logo",
            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/indian2520space2520research2520organization_logo_20190215225409.png",
            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305185007.png",
            "credit": null,
            "license": {
            "id": 1,
            "name": "Unknown",
            "priority": 9,
            "link": null
        },
            "single_use": true,
            "variants": []
        },
            "social_logo": {
            "id": 63,
            "name": "[AUTO] Indian Space Research Organization - social_logo",
            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/indian2520space2520research2520organization_nation_20230624125733.jpg",
            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184805.jpeg",
            "credit": null,
            "license": {
            "id": 1,
            "name": "Unknown",
            "priority": 9,
            "link": null
        },
            "single_use": true,
            "variants": []
        },
            "total_launch_count": 98,
            "consecutive_successful_launches": 3,
            "successful_launches": 84,
            "failed_launches": 14,
            "pending_launches": 17,
            "consecutive_successful_landings": 0,
            "successful_landings": 0,
            "failed_landings": 0,
            "attempted_landings": 0,
            "successful_landings_spacecraft": 0,
            "failed_landings_spacecraft": 0,
            "attempted_landings_spacecraft": 0,
            "successful_landings_payload": 1,
            "failed_landings_payload": 1,
            "attempted_landings_payload": 2,
            "info_url": "https://www.isro.gov.in/",
            "wiki_url": "https://en.wikipedia.org/wiki/Indian_Space_Research_Organization",
            "social_media_links": []
        },
            "rocket": {
            "id": 2413,
            "configuration": {
            "response_mode": "detailed",
            "id": 168,
            "url": "https://lldev.thespacedevs.com/2.3.0/launcher_configurations/168/",
            "name": "GSLV Mk II",
            "families": [
            {
                "response_mode": "detailed",
                "id": 84,
                "name": "GSLV",
                "manufacturer": [
                {
                    "response_mode": "normal",
                    "id": 31,
                    "url": "https://lldev.thespacedevs.com/2.3.0/agencies/31/",
                    "name": "Indian Space Research Organization",
                    "abbrev": "ISRO",
                    "type": {
                    "id": 1,
                    "name": "Government"
                },
                    "featured": true,
                    "country": [
                    {
                        "id": 9,
                        "name": "India",
                        "alpha_2_code": "IN",
                        "alpha_3_code": "IND",
                        "nationality_name": "Indian",
                        "nationality_name_composed": "Indo"
                    }
                    ],
                    "description": "The Indian Space Research Organisation (ISRO) is the space agency of the Government of India headquartered in the city of Bangalore. Its vision is to \"harness space technology for national development while pursuing space science research and planetary exploration.\"",
                    "administrator": "Chairman: V. Narayanan",
                    "founding_year": 1969,
                    "launchers": "PSLV | GSLV",
                    "spacecraft": "Gaganyaan",
                    "parent": null,
                    "image": {
                    "id": 14,
                    "name": "[AUTO] Indian Space Research Organization - image",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/indian2520space2520research2520organization_image_20190215225409.jpeg",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184615.jpeg",
                    "credit": null,
                    "license": {
                    "id": 1,
                    "name": "Unknown",
                    "priority": 9,
                    "link": null
                },
                    "single_use": true,
                    "variants": []
                },
                    "logo": {
                    "id": 141,
                    "name": "[AUTO] Indian Space Research Organization - logo",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/indian2520space2520research2520organization_logo_20190215225409.png",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305185007.png",
                    "credit": null,
                    "license": {
                    "id": 1,
                    "name": "Unknown",
                    "priority": 9,
                    "link": null
                },
                    "single_use": true,
                    "variants": []
                },
                    "social_logo": {
                    "id": 63,
                    "name": "[AUTO] Indian Space Research Organization - social_logo",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/indian2520space2520research2520organization_nation_20230624125733.jpg",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184805.jpeg",
                    "credit": null,
                    "license": {
                    "id": 1,
                    "name": "Unknown",
                    "priority": 9,
                    "link": null
                },
                    "single_use": true,
                    "variants": []
                },
                    "total_launch_count": 98,
                    "consecutive_successful_launches": 3,
                    "successful_launches": 84,
                    "failed_launches": 14,
                    "pending_launches": 17,
                    "consecutive_successful_landings": 0,
                    "successful_landings": 0,
                    "failed_landings": 0,
                    "attempted_landings": 0,
                    "successful_landings_spacecraft": 0,
                    "failed_landings_spacecraft": 0,
                    "attempted_landings_spacecraft": 0,
                    "successful_landings_payload": 1,
                    "failed_landings_payload": 1,
                    "attempted_landings_payload": 2,
                    "info_url": "https://www.isro.gov.in/",
                    "wiki_url": "https://en.wikipedia.org/wiki/Indian_Space_Research_Organization",
                    "social_media_links": []
                }
                ],
                "parent": null,
                "description": "",
                "active": true,
                "maiden_flight": "2001-04-18",
                "total_launch_count": 27,
                "consecutive_successful_launches": 9,
                "successful_launches": 21,
                "failed_launches": 6,
                "pending_launches": 11,
                "attempted_landings": 0,
                "successful_landings": 0,
                "failed_landings": 0,
                "consecutive_successful_landings": 0
            }
            ],
            "full_name": "GSLV Mk. II",
            "variant": "",
            "active": true,
            "is_placeholder": false,
            "manufacturer": {
            "response_mode": "normal",
            "id": 31,
            "url": "https://lldev.thespacedevs.com/2.3.0/agencies/31/",
            "name": "Indian Space Research Organization",
            "abbrev": "ISRO",
            "type": {
            "id": 1,
            "name": "Government"
        },
            "featured": true,
            "country": [
            {
                "id": 9,
                "name": "India",
                "alpha_2_code": "IN",
                "alpha_3_code": "IND",
                "nationality_name": "Indian",
                "nationality_name_composed": "Indo"
            }
            ],
            "description": "The Indian Space Research Organisation (ISRO) is the space agency of the Government of India headquartered in the city of Bangalore. Its vision is to \"harness space technology for national development while pursuing space science research and planetary exploration.\"",
            "administrator": "Chairman: V. Narayanan",
            "founding_year": 1969,
            "launchers": "PSLV | GSLV",
            "spacecraft": "Gaganyaan",
            "parent": null,
            "image": {
            "id": 14,
            "name": "[AUTO] Indian Space Research Organization - image",
            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/indian2520space2520research2520organization_image_20190215225409.jpeg",
            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184615.jpeg",
            "credit": null,
            "license": {
            "id": 1,
            "name": "Unknown",
            "priority": 9,
            "link": null
        },
            "single_use": true,
            "variants": []
        },
            "logo": {
            "id": 141,
            "name": "[AUTO] Indian Space Research Organization - logo",
            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/indian2520space2520research2520organization_logo_20190215225409.png",
            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305185007.png",
            "credit": null,
            "license": {
            "id": 1,
            "name": "Unknown",
            "priority": 9,
            "link": null
        },
            "single_use": true,
            "variants": []
        },
            "social_logo": {
            "id": 63,
            "name": "[AUTO] Indian Space Research Organization - social_logo",
            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/indian2520space2520research2520organization_nation_20230624125733.jpg",
            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184805.jpeg",
            "credit": null,
            "license": {
            "id": 1,
            "name": "Unknown",
            "priority": 9,
            "link": null
        },
            "single_use": true,
            "variants": []
        },
            "total_launch_count": 98,
            "consecutive_successful_launches": 3,
            "successful_launches": 84,
            "failed_launches": 14,
            "pending_launches": 17,
            "consecutive_successful_landings": 0,
            "successful_landings": 0,
            "failed_landings": 0,
            "attempted_landings": 0,
            "successful_landings_spacecraft": 0,
            "failed_landings_spacecraft": 0,
            "attempted_landings_spacecraft": 0,
            "successful_landings_payload": 1,
            "failed_landings_payload": 1,
            "attempted_landings_payload": 2,
            "info_url": "https://www.isro.gov.in/",
            "wiki_url": "https://en.wikipedia.org/wiki/Indian_Space_Research_Organization",
            "social_media_links": []
        },
            "program": [],
            "reusable": false,
            "image": {
            "id": 1744,
            "name": "[AUTO] GSLV Mk II - image",
            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/gslv2520mk2520ii_image_20190825171642.jpg",
            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305193640.jpeg",
            "credit": null,
            "license": {
            "id": 1,
            "name": "Unknown",
            "priority": 9,
            "link": null
        },
            "single_use": true,
            "variants": []
        },
            "info_url": null,
            "wiki_url": "https://en.wikipedia.org/wiki/Geosynchronous_Satellite_Launch_Vehicle",
            "description": "Geosynchronous Satellite Launch Vehicle Mark II (GSLV Mk II) is the largest launch vehicle developed by India, which is currently in operation. This fourth generation launch vehicle is a three stage vehicle with four liquid strap-ons. The indigenously developed cryogenic Upper Stage (CUS), which is flight proven, forms the third stage of GSLV Mk II. From January 2014, the vehicle has achieved four consecutive successes.",
            "alias": "",
            "min_stage": 0,
            "max_stage": 3,
            "length": 49.0,
            "diameter": 2.8,
            "maiden_flight": "2010-04-15",
            "launch_cost": null,
            "launch_mass": 401.0,
            "leo_capacity": 0.0,
            "gto_capacity": 2500.0,
            "geo_capacity": null,
            "sso_capacity": null,
            "to_thrust": 7420.0,
            "apogee": 40000.0,
            "total_launch_count": 12,
            "consecutive_successful_launches": 4,
            "successful_launches": 10,
            "failed_launches": 2,
            "pending_launches": 5,
            "attempted_landings": 0,
            "successful_landings": 0,
            "failed_landings": 0,
            "consecutive_successful_landings": 0,
            "fastest_turnaround": "P182DT11H17M"
        },
            "launcher_stage": [],
            "spacecraft_stage": [],
            "payloads": []
        },
            "mission": {
            "id": 6523,
            "name": "GISAT-1A (EOS-05)",
            "type": "Earth Science",
            "description": "GISAT-1A (GEO Imaging Satellite) is an Indian earth observing satellite operating from geostationary orbit to facilitate continuous observation of Indian sub-continent, quick monitoring of natural hazards and disaster.",
            "image": null,
            "orbit": {
            "id": 2,
            "name": "Geostationary Transfer Orbit",
            "abbrev": "GTO",
            "celestial_body": {
            "response_mode": "list",
            "id": 1,
            "name": "Earth"
        }
        },
            "agencies": [
            {
                "response_mode": "normal",
                "id": 31,
                "url": "https://lldev.thespacedevs.com/2.3.0/agencies/31/",
                "name": "Indian Space Research Organization",
                "abbrev": "ISRO",
                "type": {
                "id": 1,
                "name": "Government"
            },
                "featured": true,
                "country": [
                {
                    "id": 9,
                    "name": "India",
                    "alpha_2_code": "IN",
                    "alpha_3_code": "IND",
                    "nationality_name": "Indian",
                    "nationality_name_composed": "Indo"
                }
                ],
                "description": "The Indian Space Research Organisation (ISRO) is the space agency of the Government of India headquartered in the city of Bangalore. Its vision is to \"harness space technology for national development while pursuing space science research and planetary exploration.\"",
                "administrator": "Chairman: V. Narayanan",
                "founding_year": 1969,
                "launchers": "PSLV | GSLV",
                "spacecraft": "Gaganyaan",
                "parent": null,
                "image": {
                "id": 14,
                "name": "[AUTO] Indian Space Research Organization - image",
                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/indian2520space2520research2520organization_image_20190215225409.jpeg",
                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184615.jpeg",
                "credit": null,
                "license": {
                "id": 1,
                "name": "Unknown",
                "priority": 9,
                "link": null
            },
                "single_use": true,
                "variants": []
            },
                "logo": {
                "id": 141,
                "name": "[AUTO] Indian Space Research Organization - logo",
                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/indian2520space2520research2520organization_logo_20190215225409.png",
                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305185007.png",
                "credit": null,
                "license": {
                "id": 1,
                "name": "Unknown",
                "priority": 9,
                "link": null
            },
                "single_use": true,
                "variants": []
            },
                "social_logo": {
                "id": 63,
                "name": "[AUTO] Indian Space Research Organization - social_logo",
                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/indian2520space2520research2520organization_nation_20230624125733.jpg",
                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184805.jpeg",
                "credit": null,
                "license": {
                "id": 1,
                "name": "Unknown",
                "priority": 9,
                "link": null
            },
                "single_use": true,
                "variants": []
            },
                "total_launch_count": 98,
                "consecutive_successful_launches": 3,
                "successful_launches": 84,
                "failed_launches": 14,
                "pending_launches": 17,
                "consecutive_successful_landings": 0,
                "successful_landings": 0,
                "failed_landings": 0,
                "attempted_landings": 0,
                "successful_landings_spacecraft": 0,
                "failed_landings_spacecraft": 0,
                "attempted_landings_spacecraft": 0,
                "successful_landings_payload": 1,
                "failed_landings_payload": 1,
                "attempted_landings_payload": 2,
                "info_url": "https://www.isro.gov.in/",
                "wiki_url": "https://en.wikipedia.org/wiki/Indian_Space_Research_Organization",
                "social_media_links": []
            }
            ],
            "info_urls": [],
            "vid_urls": []
        },
            "pad": {
            "id": 82,
            "url": "https://lldev.thespacedevs.com/2.3.0/pads/82/",
            "active": true,
            "agencies": [
            {
                "response_mode": "normal",
                "id": 31,
                "url": "https://lldev.thespacedevs.com/2.3.0/agencies/31/",
                "name": "Indian Space Research Organization",
                "abbrev": "ISRO",
                "type": {
                "id": 1,
                "name": "Government"
            },
                "featured": true,
                "country": [
                {
                    "id": 9,
                    "name": "India",
                    "alpha_2_code": "IN",
                    "alpha_3_code": "IND",
                    "nationality_name": "Indian",
                    "nationality_name_composed": "Indo"
                }
                ],
                "description": "The Indian Space Research Organisation (ISRO) is the space agency of the Government of India headquartered in the city of Bangalore. Its vision is to \"harness space technology for national development while pursuing space science research and planetary exploration.\"",
                "administrator": "Chairman: V. Narayanan",
                "founding_year": 1969,
                "launchers": "PSLV | GSLV",
                "spacecraft": "Gaganyaan",
                "parent": null,
                "image": {
                "id": 14,
                "name": "[AUTO] Indian Space Research Organization - image",
                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/indian2520space2520research2520organization_image_20190215225409.jpeg",
                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184615.jpeg",
                "credit": null,
                "license": {
                "id": 1,
                "name": "Unknown",
                "priority": 9,
                "link": null
            },
                "single_use": true,
                "variants": []
            },
                "logo": {
                "id": 141,
                "name": "[AUTO] Indian Space Research Organization - logo",
                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/indian2520space2520research2520organization_logo_20190215225409.png",
                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305185007.png",
                "credit": null,
                "license": {
                "id": 1,
                "name": "Unknown",
                "priority": 9,
                "link": null
            },
                "single_use": true,
                "variants": []
            },
                "social_logo": {
                "id": 63,
                "name": "[AUTO] Indian Space Research Organization - social_logo",
                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/indian2520space2520research2520organization_nation_20230624125733.jpg",
                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184805.jpeg",
                "credit": null,
                "license": {
                "id": 1,
                "name": "Unknown",
                "priority": 9,
                "link": null
            },
                "single_use": true,
                "variants": []
            }
            }
            ],
            "name": "Satish Dhawan Space Centre Second Launch Pad",
            "image": {
            "id": 1992,
            "name": "Second Launch Pad",
            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/second_launch_p_image_20240311093204.jpeg",
            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/second_launch_p_image_thumbnail_20240311093204.jpeg",
            "credit": "Indian Space Research Organisation",
            "license": {
            "id": 11,
            "name": "Government Open Data License - India (GODL)",
            "priority": 3,
            "link": "https://data.gov.in/sites/default/files/Gazette_Notification_OGDL.pdf"
        },
            "single_use": true,
            "variants": []
        },
            "description": "The second launch pad was built between 1999 and 2003.  It became functional in 2005 and has since been used for various rockets. This launch pad supports missions by PSLV, GSLV, LVM3 rockets and is even envisioned for India's future crewed spaceflight.",
            "info_url": null,
            "wiki_url": "https://en.wikipedia.org/wiki/Satish_Dhawan_Space_Centre_Second_Launch_Pad",
            "map_url": "https://www.google.com/maps?q=13.7199,80.2304",
            "latitude": 13.7199,
            "longitude": 80.2304,
            "country": {
            "id": 9,
            "name": "India",
            "alpha_2_code": "IN",
            "alpha_3_code": "IND",
            "nationality_name": "Indian",
            "nationality_name_composed": "Indo"
        },
            "map_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/map_images/pad_82_20200803143602.jpg",
            "total_launch_count": 32,
            "orbital_launch_attempt_count": 31,
            "fastest_turnaround": "P31DT31M",
            "location": {
            "response_mode": "normal",
            "id": 14,
            "url": "https://lldev.thespacedevs.com/2.3.0/locations/14/",
            "name": "Satish Dhawan Space Centre, India",
            "celestial_body": {
            "response_mode": "normal",
            "id": 1,
            "name": "Earth",
            "type": {
            "id": 1,
            "name": "Planet"
        },
            "diameter": 12742000.0,
            "mass": 5.972168e+24,
            "gravity": 9.80655,
            "length_of_day": "1 00:00:00",
            "atmosphere": true,
            "image": {
            "id": 2040,
            "name": "Earth (Apollo 17)",
            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/earth_2528apol_image_20240402194304.jpeg",
            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/earth_2528apol_image_thumbnail_20240402194305.jpeg",
            "credit": "NASA",
            "license": {
            "id": 4,
            "name": "NASA Image and Media Guidelines",
            "priority": 0,
            "link": "https://www.nasa.gov/nasa-brand-center/images-and-media/"
        },
            "single_use": true,
            "variants": []
        },
            "description": "Earth is the third planet from the Sun and the only astronomical object known to harbor life.",
            "wiki_url": "https://en.wikipedia.org/wiki/Earth",
            "total_attempted_launches": 7388,
            "successful_launches": 6838,
            "failed_launches": 550,
            "total_attempted_landings": 1243,
            "successful_landings": 1196,
            "failed_landings": 47
        },
            "active": true,
            "country": {
            "id": 9,
            "name": "India",
            "alpha_2_code": "IN",
            "alpha_3_code": "IND",
            "nationality_name": "Indian",
            "nationality_name_composed": "Indo"
        },
            "description": "Satish Dhawan Space Centre – SDSC (formerly Sriharikota Range – SHAR),[1] is the primary spaceport of the Indian Space Research Organisation (ISRO), located in Sriharikota, Andhra Pradesh.",
            "image": {
            "id": 2212,
            "name": "Satish Dhawan Space Centre",
            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/satish_dhawan_s_image_20240919164153.png",
            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/satish_dhawan_s_image_thumbnail_20240919164154.png",
            "credit": "ISRO",
            "license": {
            "id": 11,
            "name": "Government Open Data License - India (GODL)",
            "priority": 3,
            "link": "https://data.gov.in/sites/default/files/Gazette_Notification_OGDL.pdf"
        },
            "single_use": true,
            "variants": []
        },
            "map_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/map_images/location_14_20200803142403.jpg",
            "longitude": 80.23,
            "latitude": 13.72,
            "timezone_name": "Asia/Kolkata",
            "total_launch_count": 102,
            "total_landing_count": 0
        }
        },
            "webcast_live": false,
            "program": [],
            "orbital_launch_attempt_count": 7219,
            "location_launch_attempt_count": 111,
            "pad_launch_attempt_count": 36,
            "agency_launch_attempt_count": 106,
            "orbital_launch_attempt_count_year": 43,
            "location_launch_attempt_count_year": 9,
            "pad_launch_attempt_count_year": 4,
            "agency_launch_attempt_count_year": 8,
            "flightclub_url": null,
            "updates": [
            {
                "id": 12240,
                "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/cosmic2520penguin_profile_20210817212020.png",
                "comment": "NET Q1 2026.",
                "info_url": "https://x.com/i/broadcasts/1OyKAjPpeMyGb",
                "created_by": "Cosmic_Penguin",
                "created_on": "2025-09-14T23:55:00Z"
            },
            {
                "id": 4022,
                "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/arnaud2520muller_profile_20230607144351.jpg",
                "comment": "Targeting March 2024",
                "info_url": "https://www.cgms-info.org/Agendas/GetWpFile.ashx?wid=7a49fc94-4517-4fd8-9abe-8c12f68cb5f5&aid=e6f0e59b-8d38-4691-aea1-3f33d881db20",
                "created_by": "Nosu",
                "created_on": "2023-05-11T20:35:03Z"
            }
            ],
            "info_urls": [],
            "vid_urls": [],
            "timeline": [],
            "pad_turnaround": "P59D",
            "mission_patches": []
        }"""


        val jsonEle: JsonElement = Json.parseToJsonElement(jsonString)
        val expectedResult = Launch(launchID = "17d0ec68-f466-4916-9dc5-16123eda6484", launchURL = "https://lldev.thespacedevs.com/2.3.0/launches/17d0ec68-f466-4916-9dc5-16123eda6484/", missionName = "GISAT-1A (EOS-05)", padName = "Satish Dhawan Space Centre Second Launch Pad", agencyID = "31", rocketID = "2413", rocketVersionID = "168", launchTimeUTC = ZonedDateTime.parse("2026-03-31T00:00Z"), launchTimePadLocal = ZonedDateTime.parse("2026-03-31T05:30+05:30[Asia/Kolkata]"), reusableParts = emptyList(), success = Status.TBD, windowOpen = ZonedDateTime.parse("2026-03-31T00:00Z"), windowClose = ZonedDateTime.parse("2026-03-31T00:00Z"), imageUrl = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/gslv2520mk2520ii_image_20190825171642.jpg", imageCredit = null, licenseType = "Unknown", weatherProbability = null, agencyName = "Indian Space Research Organization", rocketName = "GSLV Mk II", rocketVariantName = "GSLV Mk. II", description = "GISAT-1A (GEO Imaging Satellite) is an Indian earth observing satellite operating from geostationary orbit to facilitate continuous observation of Indian sub-continent, quick monitoring of natural hazards and disaster.", orbit = "Geostationary Transfer Orbit", padCords = Location(latitude = 13.7199, longitude = 80.2304), padLocation = "Satish Dhawan Space Centre, India", padCountry = "India", padTimeZone = "Asia/Kolkata", liveStreamUrl = "HOLD TEST", netPrecision = "The T-0 is expected in the first quarter of the given year.")
        assertThat(parseLaunch(launchJsonEle = jsonEle)).equals(expectedResult)
    }

    @Test
    fun testSingleReuse() {
        val jsonString = """
        {
    "id": "9d12fbac-53de-4517-9191-4697fbc07dd2",
    "url": "https://lldev.thespacedevs.com/2.3.0/launches/9d12fbac-53de-4517-9191-4697fbc07dd2/",
    "name": "Falcon 9 Block 5 | Starlink Group 6-99",
    "response_mode": "detailed",
    "slug": "falcon-9-block-5-starlink-group-6-99",
    "launch_designator": "2025-303",
    "status": {
        "id": 3,
        "name": "Launch Successful",
        "abbrev": "Success",
        "description": "The launch vehicle successfully inserted its payload(s) into the target orbit(s)."
    },
    "last_updated": "2025-12-17T16:41:40Z",
    "net": "2025-12-17T13:42:10Z",
    "net_precision": {
        "id": 0,
        "name": "Second",
        "abbrev": "SEC",
        "description": "The T-0 is accurate to the second."
    },
    "window_end": "2025-12-17T16:19:00Z",
    "window_start": "2025-12-17T12:19:00Z",
    "image": {
        "id": 1296,
        "name": "Starlink night fairing",
        "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/falcon2520925_image_20221009234147.png",
        "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305192320.png",
        "credit": "SpaceX",
        "license": {
            "id": 5,
            "name": "CC BY-NC 2.0",
            "priority": 1,
            "link": "https://creativecommons.org/licenses/by-nc/2.0/"
        },
        "single_use": false,
        "variants": []
    },
    "infographic": null,
    "probability": 95,
    "weather_concerns": "Cumulus Cloud Rule",
    "failreason": "",
    "hashtag": null,
    "launch_service_provider": {
        "response_mode": "normal",
        "id": 121,
        "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
        "name": "SpaceX",
        "abbrev": "SpX",
        "type": {
            "id": 3,
            "name": "Commercial"
        },
        "featured": true,
        "country": [
            {
                "id": 2,
                "name": "United States of America",
                "alpha_2_code": "US",
                "alpha_3_code": "USA",
                "nationality_name": "American",
                "nationality_name_composed": "Americano"
            }
        ],
        "description": "Space Exploration Technologies Corp., known as SpaceX, is an American aerospace manufacturer and space transport services company headquartered in Hawthorne, California. It was founded in 2002 by entrepreneur Elon Musk with the goal of reducing space transportation costs and enabling the colonization of Mars. SpaceX operates from many pads, on the East Coast of the US they operate from SLC-40 at Cape Canaveral Space Force Station and historic LC-39A at Kennedy Space Center. They also operate from SLC-4E at Vandenberg Space Force Base, California, usually for polar launches. Another launch site is being developed at Boca Chica, Texas.",
        "administrator": "CEO: Elon Musk",
        "founding_year": 2002,
        "launchers": "Falcon | Starship",
        "spacecraft": "Dragon",
        "parent": null,
        "image": {
            "id": 29,
            "name": "[AUTO] SpaceX - image",
            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_image_20190207032501.jpeg",
            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184704.jpeg",
            "credit": null,
            "license": {
                "id": 1,
                "name": "Unknown",
                "priority": 9,
                "link": null
            },
            "single_use": true,
            "variants": []
        },
        "logo": {
            "id": 186,
            "name": "[AUTO] SpaceX - logo",
            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_logo_20220826094919.png",
            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305185138.png",
            "credit": null,
            "license": {
                "id": 1,
                "name": "Unknown",
                "priority": 9,
                "link": null
            },
            "single_use": true,
            "variants": []
        },
        "social_logo": {
            "id": 94,
            "name": "[AUTO] SpaceX - social_logo",
            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_nation_20230531064544.jpg",
            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184848.jpeg",
            "credit": null,
            "license": {
                "id": 1,
                "name": "Unknown",
                "priority": 9,
                "link": null
            },
            "single_use": true,
            "variants": []
        },
        "total_launch_count": 619,
        "consecutive_successful_launches": 104,
        "successful_launches": 604,
        "failed_launches": 15,
        "pending_launches": 116,
        "consecutive_successful_landings": 103,
        "successful_landings": 564,
        "failed_landings": 27,
        "attempted_landings": 590,
        "successful_landings_spacecraft": 58,
        "failed_landings_spacecraft": 3,
        "attempted_landings_spacecraft": 61,
        "successful_landings_payload": 0,
        "failed_landings_payload": 0,
        "attempted_landings_payload": 0,
        "info_url": "https://www.spacex.com/",
        "wiki_url": "https://en.wikipedia.org/wiki/SpaceX",
        "social_media_links": []
    },
    "rocket": {
        "id": 8839,
        "configuration": {
            "response_mode": "detailed",
            "id": 164,
            "url": "https://lldev.thespacedevs.com/2.3.0/launcher_configurations/164/",
            "name": "Falcon 9",
            "families": [
                {
                    "response_mode": "detailed",
                    "id": 1,
                    "name": "Falcon",
                    "manufacturer": [
                        {
                            "response_mode": "normal",
                            "id": 121,
                            "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
                            "name": "SpaceX",
                            "abbrev": "SpX",
                            "type": {
                                "id": 3,
                                "name": "Commercial"
                            },
                            "featured": true,
                            "country": [
                                {
                                    "id": 2,
                                    "name": "United States of America",
                                    "alpha_2_code": "US",
                                    "alpha_3_code": "USA",
                                    "nationality_name": "American",
                                    "nationality_name_composed": "Americano"
                                }
                            ],
                            "description": "Space Exploration Technologies Corp., known as SpaceX, is an American aerospace manufacturer and space transport services company headquartered in Hawthorne, California. It was founded in 2002 by entrepreneur Elon Musk with the goal of reducing space transportation costs and enabling the colonization of Mars. SpaceX operates from many pads, on the East Coast of the US they operate from SLC-40 at Cape Canaveral Space Force Station and historic LC-39A at Kennedy Space Center. They also operate from SLC-4E at Vandenberg Space Force Base, California, usually for polar launches. Another launch site is being developed at Boca Chica, Texas.",
                            "administrator": "CEO: Elon Musk",
                            "founding_year": 2002,
                            "launchers": "Falcon | Starship",
                            "spacecraft": "Dragon",
                            "parent": null,
                            "image": {
                                "id": 29,
                                "name": "[AUTO] SpaceX - image",
                                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_image_20190207032501.jpeg",
                                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184704.jpeg",
                                "credit": null,
                                "license": {
                                    "id": 1,
                                    "name": "Unknown",
                                    "priority": 9,
                                    "link": null
                                },
                                "single_use": true,
                                "variants": []
                            },
                            "logo": {
                                "id": 186,
                                "name": "[AUTO] SpaceX - logo",
                                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_logo_20220826094919.png",
                                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305185138.png",
                                "credit": null,
                                "license": {
                                    "id": 1,
                                    "name": "Unknown",
                                    "priority": 9,
                                    "link": null
                                },
                                "single_use": true,
                                "variants": []
                            },
                            "social_logo": {
                                "id": 94,
                                "name": "[AUTO] SpaceX - social_logo",
                                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_nation_20230531064544.jpg",
                                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184848.jpeg",
                                "credit": null,
                                "license": {
                                    "id": 1,
                                    "name": "Unknown",
                                    "priority": 9,
                                    "link": null
                                },
                                "single_use": true,
                                "variants": []
                            },
                            "total_launch_count": 619,
                            "consecutive_successful_launches": 104,
                            "successful_launches": 604,
                            "failed_launches": 15,
                            "pending_launches": 116,
                            "consecutive_successful_landings": 103,
                            "successful_landings": 564,
                            "failed_landings": 27,
                            "attempted_landings": 590,
                            "successful_landings_spacecraft": 58,
                            "failed_landings_spacecraft": 3,
                            "attempted_landings_spacecraft": 61,
                            "successful_landings_payload": 0,
                            "failed_landings_payload": 0,
                            "attempted_landings_payload": 0,
                            "info_url": "https://www.spacex.com/",
                            "wiki_url": "https://en.wikipedia.org/wiki/SpaceX",
                            "social_media_links": []
                        }
                    ],
                    "parent": null,
                    "description": "Family of two-stage partially reusable orbital launch vehicles developed by SpaceX.",
                    "active": true,
                    "maiden_flight": "2006-03-24",
                    "total_launch_count": 599,
                    "consecutive_successful_launches": 229,
                    "successful_launches": 592,
                    "failed_launches": 7,
                    "pending_launches": 113,
                    "attempted_landings": 571,
                    "successful_landings": 553,
                    "failed_landings": 19,
                    "consecutive_successful_landings": 212
                },
                {
                    "response_mode": "detailed",
                    "id": 176,
                    "name": "Falcon 9",
                    "manufacturer": [
                        {
                            "response_mode": "normal",
                            "id": 121,
                            "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
                            "name": "SpaceX",
                            "abbrev": "SpX",
                            "type": {
                                "id": 3,
                                "name": "Commercial"
                            },
                            "featured": true,
                            "country": [
                                {
                                    "id": 2,
                                    "name": "United States of America",
                                    "alpha_2_code": "US",
                                    "alpha_3_code": "USA",
                                    "nationality_name": "American",
                                    "nationality_name_composed": "Americano"
                                }
                            ],
                            "description": "Space Exploration Technologies Corp., known as SpaceX, is an American aerospace manufacturer and space transport services company headquartered in Hawthorne, California. It was founded in 2002 by entrepreneur Elon Musk with the goal of reducing space transportation costs and enabling the colonization of Mars. SpaceX operates from many pads, on the East Coast of the US they operate from SLC-40 at Cape Canaveral Space Force Station and historic LC-39A at Kennedy Space Center. They also operate from SLC-4E at Vandenberg Space Force Base, California, usually for polar launches. Another launch site is being developed at Boca Chica, Texas.",
                            "administrator": "CEO: Elon Musk",
                            "founding_year": 2002,
                            "launchers": "Falcon | Starship",
                            "spacecraft": "Dragon",
                            "parent": null,
                            "image": {
                                "id": 29,
                                "name": "[AUTO] SpaceX - image",
                                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_image_20190207032501.jpeg",
                                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184704.jpeg",
                                "credit": null,
                                "license": {
                                    "id": 1,
                                    "name": "Unknown",
                                    "priority": 9,
                                    "link": null
                                },
                                "single_use": true,
                                "variants": []
                            },
                            "logo": {
                                "id": 186,
                                "name": "[AUTO] SpaceX - logo",
                                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_logo_20220826094919.png",
                                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305185138.png",
                                "credit": null,
                                "license": {
                                    "id": 1,
                                    "name": "Unknown",
                                    "priority": 9,
                                    "link": null
                                },
                                "single_use": true,
                                "variants": []
                            },
                            "social_logo": {
                                "id": 94,
                                "name": "[AUTO] SpaceX - social_logo",
                                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_nation_20230531064544.jpg",
                                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184848.jpeg",
                                "credit": null,
                                "license": {
                                    "id": 1,
                                    "name": "Unknown",
                                    "priority": 9,
                                    "link": null
                                },
                                "single_use": true,
                                "variants": []
                            },
                            "total_launch_count": 619,
                            "consecutive_successful_launches": 104,
                            "successful_launches": 604,
                            "failed_launches": 15,
                            "pending_launches": 116,
                            "consecutive_successful_landings": 103,
                            "successful_landings": 564,
                            "failed_landings": 27,
                            "attempted_landings": 590,
                            "successful_landings_spacecraft": 58,
                            "failed_landings_spacecraft": 3,
                            "attempted_landings_spacecraft": 61,
                            "successful_landings_payload": 0,
                            "failed_landings_payload": 0,
                            "attempted_landings_payload": 0,
                            "info_url": "https://www.spacex.com/",
                            "wiki_url": "https://en.wikipedia.org/wiki/SpaceX",
                            "social_media_links": []
                        }
                    ],
                    "parent": {
                        "response_mode": "normal",
                        "id": 1,
                        "name": "Falcon",
                        "manufacturer": [
                            {
                                "response_mode": "normal",
                                "id": 121,
                                "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
                                "name": "SpaceX",
                                "abbrev": "SpX",
                                "type": {
                                    "id": 3,
                                    "name": "Commercial"
                                },
                                "featured": true,
                                "country": [
                                    {
                                        "id": 2,
                                        "name": "United States of America",
                                        "alpha_2_code": "US",
                                        "alpha_3_code": "USA",
                                        "nationality_name": "American",
                                        "nationality_name_composed": "Americano"
                                    }
                                ],
                                "description": "Space Exploration Technologies Corp., known as SpaceX, is an American aerospace manufacturer and space transport services company headquartered in Hawthorne, California. It was founded in 2002 by entrepreneur Elon Musk with the goal of reducing space transportation costs and enabling the colonization of Mars. SpaceX operates from many pads, on the East Coast of the US they operate from SLC-40 at Cape Canaveral Space Force Station and historic LC-39A at Kennedy Space Center. They also operate from SLC-4E at Vandenberg Space Force Base, California, usually for polar launches. Another launch site is being developed at Boca Chica, Texas.",
                                "administrator": "CEO: Elon Musk",
                                "founding_year": 2002,
                                "launchers": "Falcon | Starship",
                                "spacecraft": "Dragon",
                                "parent": null,
                                "image": {
                                    "id": 29,
                                    "name": "[AUTO] SpaceX - image",
                                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_image_20190207032501.jpeg",
                                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184704.jpeg",
                                    "credit": null,
                                    "license": {
                                        "id": 1,
                                        "name": "Unknown",
                                        "priority": 9,
                                        "link": null
                                    },
                                    "single_use": true,
                                    "variants": []
                                },
                                "logo": {
                                    "id": 186,
                                    "name": "[AUTO] SpaceX - logo",
                                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_logo_20220826094919.png",
                                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305185138.png",
                                    "credit": null,
                                    "license": {
                                        "id": 1,
                                        "name": "Unknown",
                                        "priority": 9,
                                        "link": null
                                    },
                                    "single_use": true,
                                    "variants": []
                                },
                                "social_logo": {
                                    "id": 94,
                                    "name": "[AUTO] SpaceX - social_logo",
                                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_nation_20230531064544.jpg",
                                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184848.jpeg",
                                    "credit": null,
                                    "license": {
                                        "id": 1,
                                        "name": "Unknown",
                                        "priority": 9,
                                        "link": null
                                    },
                                    "single_use": true,
                                    "variants": []
                                }
                            }
                        ],
                        "parent": null
                    },
                    "description": "",
                    "active": true,
                    "maiden_flight": "2010-06-04",
                    "total_launch_count": 583,
                    "consecutive_successful_launches": 228,
                    "successful_launches": 579,
                    "failed_launches": 4,
                    "pending_launches": 98,
                    "attempted_landings": 548,
                    "successful_landings": 534,
                    "failed_landings": 15,
                    "consecutive_successful_landings": 212
                }
            ],
            "full_name": "Falcon 9 Block 5",
            "variant": "Block 5",
            "active": true,
            "is_placeholder": false,
            "manufacturer": {
                "response_mode": "normal",
                "id": 121,
                "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
                "name": "SpaceX",
                "abbrev": "SpX",
                "type": {
                    "id": 3,
                    "name": "Commercial"
                },
                "featured": true,
                "country": [
                    {
                        "id": 2,
                        "name": "United States of America",
                        "alpha_2_code": "US",
                        "alpha_3_code": "USA",
                        "nationality_name": "American",
                        "nationality_name_composed": "Americano"
                    }
                ],
                "description": "Space Exploration Technologies Corp., known as SpaceX, is an American aerospace manufacturer and space transport services company headquartered in Hawthorne, California. It was founded in 2002 by entrepreneur Elon Musk with the goal of reducing space transportation costs and enabling the colonization of Mars. SpaceX operates from many pads, on the East Coast of the US they operate from SLC-40 at Cape Canaveral Space Force Station and historic LC-39A at Kennedy Space Center. They also operate from SLC-4E at Vandenberg Space Force Base, California, usually for polar launches. Another launch site is being developed at Boca Chica, Texas.",
                "administrator": "CEO: Elon Musk",
                "founding_year": 2002,
                "launchers": "Falcon | Starship",
                "spacecraft": "Dragon",
                "parent": null,
                "image": {
                    "id": 29,
                    "name": "[AUTO] SpaceX - image",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_image_20190207032501.jpeg",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184704.jpeg",
                    "credit": null,
                    "license": {
                        "id": 1,
                        "name": "Unknown",
                        "priority": 9,
                        "link": null
                    },
                    "single_use": true,
                    "variants": []
                },
                "logo": {
                    "id": 186,
                    "name": "[AUTO] SpaceX - logo",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_logo_20220826094919.png",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305185138.png",
                    "credit": null,
                    "license": {
                        "id": 1,
                        "name": "Unknown",
                        "priority": 9,
                        "link": null
                    },
                    "single_use": true,
                    "variants": []
                },
                "social_logo": {
                    "id": 94,
                    "name": "[AUTO] SpaceX - social_logo",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_nation_20230531064544.jpg",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184848.jpeg",
                    "credit": null,
                    "license": {
                        "id": 1,
                        "name": "Unknown",
                        "priority": 9,
                        "link": null
                    },
                    "single_use": true,
                    "variants": []
                },
                "total_launch_count": 619,
                "consecutive_successful_launches": 104,
                "successful_launches": 604,
                "failed_launches": 15,
                "pending_launches": 116,
                "consecutive_successful_landings": 103,
                "successful_landings": 564,
                "failed_landings": 27,
                "attempted_landings": 590,
                "successful_landings_spacecraft": 58,
                "failed_landings_spacecraft": 3,
                "attempted_landings_spacecraft": 61,
                "successful_landings_payload": 0,
                "failed_landings_payload": 0,
                "attempted_landings_payload": 0,
                "info_url": "https://www.spacex.com/",
                "wiki_url": "https://en.wikipedia.org/wiki/SpaceX",
                "social_media_links": []
            },
            "program": [
                {
                    "response_mode": "normal",
                    "id": 11,
                    "url": "https://lldev.thespacedevs.com/2.3.0/programs/11/",
                    "name": "Commercial Resupply Services",
                    "image": {
                        "id": 1878,
                        "name": "[AUTO] Commercial Resupply Services - image",
                        "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/commercial2520_program_20201129212219.png",
                        "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305194021.png",
                        "credit": null,
                        "license": {
                            "id": 1,
                            "name": "Unknown",
                            "priority": 9,
                            "link": null
                        },
                        "single_use": true,
                        "variants": []
                    },
                    "info_url": null,
                    "wiki_url": "https://en.wikipedia.org/wiki/Commercial_Resupply_Services#Commercial_Resupply_Services",
                    "description": "Commercial Resupply Services (CRS) are a series of flights awarded by NASA for the delivery of cargo and supplies to the International Space Station.The first CRS contracts were signed in 2008 and awarded $1.6 billion to SpaceX for twelve cargo Dragon and $1.9 billion to Orbital Sciences for eight Cygnus flights, covering deliveries to 2016. The Falcon 9 and Antares rockets were also developed under the CRS program to deliver cargo spacecraft to the ISS.",
                    "agencies": [
                        {
                            "response_mode": "list",
                            "id": 44,
                            "url": "https://lldev.thespacedevs.com/2.3.0/agencies/44/",
                            "name": "National Aeronautics and Space Administration",
                            "abbrev": "NASA",
                            "type": {
                                "id": 1,
                                "name": "Government"
                            }
                        },
                        {
                            "response_mode": "list",
                            "id": 257,
                            "url": "https://lldev.thespacedevs.com/2.3.0/agencies/257/",
                            "name": "Northrop Grumman Space Systems",
                            "abbrev": "NGSS",
                            "type": {
                                "id": 3,
                                "name": "Commercial"
                            }
                        },
                        {
                            "response_mode": "list",
                            "id": 1020,
                            "url": "https://lldev.thespacedevs.com/2.3.0/agencies/1020/",
                            "name": "Sierra Nevada Corporation",
                            "abbrev": "SNC",
                            "type": {
                                "id": 3,
                                "name": "Commercial"
                            }
                        },
                        {
                            "response_mode": "list",
                            "id": 121,
                            "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
                            "name": "SpaceX",
                            "abbrev": "SpX",
                            "type": {
                                "id": 3,
                                "name": "Commercial"
                            }
                        }
                    ],
                    "start_date": "2008-12-23T00:00:00Z",
                    "end_date": null,
                    "mission_patches": [],
                    "type": {
                        "id": 2,
                        "name": "Human Spaceflight"
                    }
                },
                {
                    "response_mode": "normal",
                    "id": 5,
                    "url": "https://lldev.thespacedevs.com/2.3.0/programs/5/",
                    "name": "Commercial Crew Program",
                    "image": {
                        "id": 1869,
                        "name": "Commercial Crew Program logo",
                        "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/commercial2520_program_20200820201209.png",
                        "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305194004.png",
                        "credit": "NASA",
                        "license": {
                            "id": 4,
                            "name": "NASA Image and Media Guidelines",
                            "priority": 0,
                            "link": "https://www.nasa.gov/nasa-brand-center/images-and-media/"
                        },
                        "single_use": true,
                        "variants": []
                    },
                    "info_url": "https://www.nasa.gov/exploration/commercial/crew/index.html",
                    "wiki_url": "https://en.wikipedia.org/wiki/Commercial_Crew_Program",
                    "description": "The Commercial Crew Program (CCP) is a human spaceflight program operated by NASA, in association with American aerospace manufacturers Boeing and SpaceX. The program conducts rotations between the expeditions of the International Space Station program, transporting crews to and from the International Space Station (ISS) aboard Boeing Starliner and SpaceX Crew Dragon capsules, in the first crewed orbital spaceflights operated by private companies.",
                    "agencies": [
                        {
                            "response_mode": "list",
                            "id": 80,
                            "url": "https://lldev.thespacedevs.com/2.3.0/agencies/80/",
                            "name": "Boeing",
                            "abbrev": "BA",
                            "type": {
                                "id": 3,
                                "name": "Commercial"
                            }
                        },
                        {
                            "response_mode": "list",
                            "id": 44,
                            "url": "https://lldev.thespacedevs.com/2.3.0/agencies/44/",
                            "name": "National Aeronautics and Space Administration",
                            "abbrev": "NASA",
                            "type": {
                                "id": 1,
                                "name": "Government"
                            }
                        },
                        {
                            "response_mode": "list",
                            "id": 121,
                            "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
                            "name": "SpaceX",
                            "abbrev": "SpX",
                            "type": {
                                "id": 3,
                                "name": "Commercial"
                            }
                        }
                    ],
                    "start_date": "2011-04-18T00:00:00Z",
                    "end_date": null,
                    "mission_patches": [],
                    "type": {
                        "id": 2,
                        "name": "Human Spaceflight"
                    }
                }
            ],
            "reusable": true,
            "image": {
                "id": 1736,
                "name": "[AUTO] Falcon 9 - image",
                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/falcon_9_image_20230807133459.jpeg",
                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305193628.jpeg",
                "credit": null,
                "license": {
                    "id": 1,
                    "name": "Unknown",
                    "priority": 9,
                    "link": null
                },
                "single_use": true,
                "variants": []
            },
            "info_url": "https://www.spacex.com/vehicles/falcon-9/",
            "wiki_url": "https://en.wikipedia.org/wiki/Falcon_9",
            "description": "Falcon 9 is a two-stage rocket designed and manufactured by SpaceX for the reliable and safe transport of satellites and the Dragon spacecraft into orbit. The Block 5 variant is the fifth major interval aimed at improving upon the ability for rapid reusability.",
            "alias": "",
            "min_stage": 2,
            "max_stage": 2,
            "length": 70.0,
            "diameter": 3.65,
            "maiden_flight": "2018-05-11",
            "launch_cost": 52000000,
            "launch_mass": 549.0,
            "leo_capacity": 22800.0,
            "gto_capacity": 8300.0,
            "geo_capacity": null,
            "sso_capacity": null,
            "to_thrust": 7607.0,
            "apogee": 200.0,
            "total_launch_count": 526,
            "consecutive_successful_launches": 228,
            "successful_launches": 525,
            "failed_launches": 1,
            "pending_launches": 98,
            "attempted_landings": 517,
            "successful_landings": 512,
            "failed_landings": 5,
            "consecutive_successful_landings": 212,
            "fastest_turnaround": "PT1H5M"
        },
        "launcher_stage": [
            {
                "id": 855,
                "type": "Core",
                "reused": true,
                "launcher_flight_number": 6,
                "launcher": {
                    "response_mode": "normal",
                    "id": 194,
                    "url": "https://lldev.thespacedevs.com/2.3.0/launchers/194/",
                    "flight_proven": true,
                    "serial_number": "B1094",
                    "is_placeholder": false,
                    "status": {
                        "id": 3,
                        "name": "active"
                    },
                    "image": {
                        "id": 2456,
                        "name": "B1094 landing on LZ-1 during Crew-11 launch",
                        "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/b1094_landing_o_image_20250912150803.jpeg",
                        "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/b1094_landing_o_image_thumbnail_20250912150803.jpeg",
                        "credit": "SpaceX",
                        "license": {
                            "id": 1,
                            "name": "Unknown",
                            "priority": 9,
                            "link": null
                        },
                        "single_use": false,
                        "variants": []
                    },
                    "details": "Falcon 9 booster debuting on Starlink Group 12-10.",
                    "successful_landings": 6,
                    "attempted_landings": 6,
                    "flights": 6,
                    "last_launch_date": "2025-12-17T13:42:10Z",
                    "first_launch_date": "2025-04-29T02:34:10Z",
                    "fastest_turnaround": "P37DT9H11M50S"
                },
                "previous_flight_date": "2025-11-06T01:31:10Z",
                "turn_around_time": "P41DT12H11M",
                "landing": {
                    "id": 1751,
                    "url": "https://lldev.thespacedevs.com/2.3.0/landings/1751/",
                    "attempt": true,
                    "success": true,
                    "description": "The Falcon 9 first stage B1094 has landed on ASDS JRTI after its 6th flight.",
                    "downrange_distance": 600.0,
                    "landing_location": {
                        "id": 3,
                        "name": "Just Read the Instructions",
                        "active": true,
                        "abbrev": "JRTI",
                        "description": "Third (Marmac 303) ASDS barge, Just Read the Instructions (JRTI) is currently used to recover Falcon 9 and Heavy boosters in the Atlantic Ocean.",
                        "location": null,
                        "longitude": null,
                        "latitude": null,
                        "image": {
                            "id": 1973,
                            "name": "JRTI (Marmac 303) empty",
                            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/jrti_2528marma_image_20240310170452.jpeg",
                            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/jrti_2528marma_image_thumbnail_20240310170452.jpeg",
                            "credit": "SpaceX",
                            "license": {
                                "id": 5,
                                "name": "CC BY-NC 2.0",
                                "priority": 1,
                                "link": "https://creativecommons.org/licenses/by-nc/2.0/"
                            },
                            "single_use": true,
                            "variants": []
                        },
                        "successful_landings": 145,
                        "attempted_landings": 146,
                        "failed_landings": 1,
                        "celestial_body": {
                            "response_mode": "normal",
                            "id": 1,
                            "name": "Earth",
                            "type": {
                                "id": 1,
                                "name": "Planet"
                            },
                            "diameter": 12742000.0,
                            "mass": 5.972168e+24,
                            "gravity": 9.80655,
                            "length_of_day": "1 00:00:00",
                            "atmosphere": true,
                            "image": {
                                "id": 2040,
                                "name": "Earth (Apollo 17)",
                                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/earth_2528apol_image_20240402194304.jpeg",
                                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/earth_2528apol_image_thumbnail_20240402194305.jpeg",
                                "credit": "NASA",
                                "license": {
                                    "id": 4,
                                    "name": "NASA Image and Media Guidelines",
                                    "priority": 0,
                                    "link": "https://www.nasa.gov/nasa-brand-center/images-and-media/"
                                },
                                "single_use": true,
                                "variants": []
                            },
                            "description": "Earth is the third planet from the Sun and the only astronomical object known to harbor life.",
                            "wiki_url": "https://en.wikipedia.org/wiki/Earth"
                        }
                    },
                    "type": {
                        "id": 1,
                        "name": "Autonomous Spaceport Drone Ship",
                        "abbrev": "ASDS",
                        "description": "An autonomous spaceport drone ship (ASDS) is an ocean-going vessel derived from a deck barge, outfitted with station-keeping engines and a large landing platform. Construction of such ships was commissioned by aerospace company SpaceX to allow for recovery of rocket first-stages at sea for high-velocity missions which do not carry enough fuel to return to the launch site after lofting spacecraft onto an orbital trajectory."
                    }
                },
                "previous_flight": {
                    "id": "9dd2d2b7-302b-4e9e-804c-aa176f606b6f",
                    "url": "https://lldev.thespacedevs.com/2.3.0/launches/9dd2d2b7-302b-4e9e-804c-aa176f606b6f/",
                    "name": "Falcon 9 Block 5 | Starlink Group 6-81"
                }
            }
        ],
        "spacecraft_stage": [],
        "payloads": []
    },
    "mission": {
        "id": 7430,
        "name": "Starlink Group 6-99",
        "type": "Communications",
        "description": "A batch of 29 satellites for the Starlink mega-constellation - SpaceX's project for space-based Internet communication system.",
        "image": null,
        "orbit": {
            "id": 8,
            "name": "Low Earth Orbit",
            "abbrev": "LEO",
            "celestial_body": {
                "response_mode": "list",
                "id": 1,
                "name": "Earth"
            }
        },
        "agencies": [
            {
                "response_mode": "normal",
                "id": 121,
                "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
                "name": "SpaceX",
                "abbrev": "SpX",
                "type": {
                    "id": 3,
                    "name": "Commercial"
                },
                "featured": true,
                "country": [
                    {
                        "id": 2,
                        "name": "United States of America",
                        "alpha_2_code": "US",
                        "alpha_3_code": "USA",
                        "nationality_name": "American",
                        "nationality_name_composed": "Americano"
                    }
                ],
                "description": "Space Exploration Technologies Corp., known as SpaceX, is an American aerospace manufacturer and space transport services company headquartered in Hawthorne, California. It was founded in 2002 by entrepreneur Elon Musk with the goal of reducing space transportation costs and enabling the colonization of Mars. SpaceX operates from many pads, on the East Coast of the US they operate from SLC-40 at Cape Canaveral Space Force Station and historic LC-39A at Kennedy Space Center. They also operate from SLC-4E at Vandenberg Space Force Base, California, usually for polar launches. Another launch site is being developed at Boca Chica, Texas.",
                "administrator": "CEO: Elon Musk",
                "founding_year": 2002,
                "launchers": "Falcon | Starship",
                "spacecraft": "Dragon",
                "parent": null,
                "image": {
                    "id": 29,
                    "name": "[AUTO] SpaceX - image",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_image_20190207032501.jpeg",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184704.jpeg",
                    "credit": null,
                    "license": {
                        "id": 1,
                        "name": "Unknown",
                        "priority": 9,
                        "link": null
                    },
                    "single_use": true,
                    "variants": []
                },
                "logo": {
                    "id": 186,
                    "name": "[AUTO] SpaceX - logo",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_logo_20220826094919.png",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305185138.png",
                    "credit": null,
                    "license": {
                        "id": 1,
                        "name": "Unknown",
                        "priority": 9,
                        "link": null
                    },
                    "single_use": true,
                    "variants": []
                },
                "social_logo": {
                    "id": 94,
                    "name": "[AUTO] SpaceX - social_logo",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_nation_20230531064544.jpg",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184848.jpeg",
                    "credit": null,
                    "license": {
                        "id": 1,
                        "name": "Unknown",
                        "priority": 9,
                        "link": null
                    },
                    "single_use": true,
                    "variants": []
                },
                "total_launch_count": 619,
                "consecutive_successful_launches": 104,
                "successful_launches": 604,
                "failed_launches": 15,
                "pending_launches": 116,
                "consecutive_successful_landings": 103,
                "successful_landings": 564,
                "failed_landings": 27,
                "attempted_landings": 590,
                "successful_landings_spacecraft": 58,
                "failed_landings_spacecraft": 3,
                "attempted_landings_spacecraft": 61,
                "successful_landings_payload": 0,
                "failed_landings_payload": 0,
                "attempted_landings_payload": 0,
                "info_url": "https://www.spacex.com/",
                "wiki_url": "https://en.wikipedia.org/wiki/SpaceX",
                "social_media_links": []
            }
        ],
        "info_urls": [],
        "vid_urls": []
    },
    "pad": {
        "id": 87,
        "url": "https://lldev.thespacedevs.com/2.3.0/pads/87/",
        "active": true,
        "agencies": [
            {
                "response_mode": "normal",
                "id": 121,
                "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
                "name": "SpaceX",
                "abbrev": "SpX",
                "type": {
                    "id": 3,
                    "name": "Commercial"
                },
                "featured": true,
                "country": [
                    {
                        "id": 2,
                        "name": "United States of America",
                        "alpha_2_code": "US",
                        "alpha_3_code": "USA",
                        "nationality_name": "American",
                        "nationality_name_composed": "Americano"
                    }
                ],
                "description": "Space Exploration Technologies Corp., known as SpaceX, is an American aerospace manufacturer and space transport services company headquartered in Hawthorne, California. It was founded in 2002 by entrepreneur Elon Musk with the goal of reducing space transportation costs and enabling the colonization of Mars. SpaceX operates from many pads, on the East Coast of the US they operate from SLC-40 at Cape Canaveral Space Force Station and historic LC-39A at Kennedy Space Center. They also operate from SLC-4E at Vandenberg Space Force Base, California, usually for polar launches. Another launch site is being developed at Boca Chica, Texas.",
                "administrator": "CEO: Elon Musk",
                "founding_year": 2002,
                "launchers": "Falcon | Starship",
                "spacecraft": "Dragon",
                "parent": null,
                "image": {
                    "id": 29,
                    "name": "[AUTO] SpaceX - image",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_image_20190207032501.jpeg",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184704.jpeg",
                    "credit": null,
                    "license": {
                        "id": 1,
                        "name": "Unknown",
                        "priority": 9,
                        "link": null
                    },
                    "single_use": true,
                    "variants": []
                },
                "logo": {
                    "id": 186,
                    "name": "[AUTO] SpaceX - logo",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_logo_20220826094919.png",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305185138.png",
                    "credit": null,
                    "license": {
                        "id": 1,
                        "name": "Unknown",
                        "priority": 9,
                        "link": null
                    },
                    "single_use": true,
                    "variants": []
                },
                "social_logo": {
                    "id": 94,
                    "name": "[AUTO] SpaceX - social_logo",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_nation_20230531064544.jpg",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184848.jpeg",
                    "credit": null,
                    "license": {
                        "id": 1,
                        "name": "Unknown",
                        "priority": 9,
                        "link": null
                    },
                    "single_use": true,
                    "variants": []
                }
            }
        ],
        "name": "Launch Complex 39A",
        "image": {
            "id": 2119,
            "name": "FH liftoff from LC-39A (GOES-U)",
            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/fh_liftoff_from_image_20240626200232.jpeg",
            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/fh_liftoff_from_image_thumbnail_20240626200233.jpeg",
            "credit": "SpaceX",
            "license": {
                "id": 5,
                "name": "CC BY-NC 2.0",
                "priority": 1,
                "link": "https://creativecommons.org/licenses/by-nc/2.0/"
            },
            "single_use": true,
            "variants": []
        },
        "description": "",
        "info_url": null,
        "wiki_url": "https://en.wikipedia.org/wiki/Kennedy_Space_Center_Launch_Complex_39#Launch_Pad_39A",
        "map_url": "https://www.google.com/maps?q=28.60822681,-80.60428186",
        "latitude": 28.60822681,
        "longitude": -80.60428186,
        "country": {
            "id": 2,
            "name": "United States of America",
            "alpha_2_code": "US",
            "alpha_3_code": "USA",
            "nationality_name": "American",
            "nationality_name_composed": "Americano"
        },
        "map_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/map_images/pad_87_20200803143537.jpg",
        "total_launch_count": 221,
        "orbital_launch_attempt_count": 220,
        "fastest_turnaround": "P5DT6H20M30S",
        "location": {
            "response_mode": "normal",
            "id": 27,
            "url": "https://lldev.thespacedevs.com/2.3.0/locations/27/",
            "name": "Kennedy Space Center, FL, USA",
            "celestial_body": {
                "response_mode": "normal",
                "id": 1,
                "name": "Earth",
                "type": {
                    "id": 1,
                    "name": "Planet"
                },
                "diameter": 12742000.0,
                "mass": 5.972168e+24,
                "gravity": 9.80655,
                "length_of_day": "1 00:00:00",
                "atmosphere": true,
                "image": {
                    "id": 2040,
                    "name": "Earth (Apollo 17)",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/earth_2528apol_image_20240402194304.jpeg",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/earth_2528apol_image_thumbnail_20240402194305.jpeg",
                    "credit": "NASA",
                    "license": {
                        "id": 4,
                        "name": "NASA Image and Media Guidelines",
                        "priority": 0,
                        "link": "https://www.nasa.gov/nasa-brand-center/images-and-media/"
                    },
                    "single_use": true,
                    "variants": []
                },
                "description": "Earth is the third planet from the Sun and the only astronomical object known to harbor life.",
                "wiki_url": "https://en.wikipedia.org/wiki/Earth",
                "total_attempted_launches": 7388,
                "successful_launches": 6838,
                "failed_launches": 550,
                "total_attempted_landings": 1243,
                "successful_landings": 1196,
                "failed_landings": 47
            },
            "active": true,
            "country": {
                "id": 2,
                "name": "United States of America",
                "alpha_2_code": "US",
                "alpha_3_code": "USA",
                "nationality_name": "American",
                "nationality_name_composed": "Americano"
            },
            "description": "The John F. Kennedy Space Center, located on Merritt Island, Florida, is one of NASA's ten field centers. Since 1968, KSC has been NASA's primary launch center of American spaceflight, research, and technology. Launch operations for the Apollo, Skylab and Space Shuttle programs were carried out from Kennedy Space Center Launch Complex 39 and managed by KSC. Located on the east coast of Florida, KSC is adjacent to Cape Canaveral Space Force Station (CCSFS).",
            "image": {
                "id": 2200,
                "name": "Cape Canaveral & KSC seen from orbit (STS-43)",
                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/cape_canaveral__image_20240918151615.jpg",
                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/cape_canaveral__image_thumbnail_20240918151616.jpeg",
                "credit": "NASA",
                "license": {
                    "id": 4,
                    "name": "NASA Image and Media Guidelines",
                    "priority": 0,
                    "link": "https://www.nasa.gov/nasa-brand-center/images-and-media/"
                },
                "single_use": true,
                "variants": []
            },
            "map_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/map_images/location_27_20200803142447.jpg",
            "longitude": -80.650833,
            "latitude": 28.524167,
            "timezone_name": "America/New_York",
            "total_launch_count": 279,
            "total_landing_count": 0
        }
    },
    "webcast_live": false,
    "program": [
        {
            "response_mode": "normal",
            "id": 25,
            "url": "https://lldev.thespacedevs.com/2.3.0/programs/25/",
            "name": "Starlink",
            "image": {
                "id": 1890,
                "name": "Starlink stack in space before deployment",
                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/starlink_program_20231228154508.jpeg",
                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305194044.jpeg",
                "credit": "SpaceX",
                "license": {
                    "id": 5,
                    "name": "CC BY-NC 2.0",
                    "priority": 1,
                    "link": "https://creativecommons.org/licenses/by-nc/2.0/"
                },
                "single_use": true,
                "variants": []
            },
            "info_url": "https://starlink.com",
            "wiki_url": "https://en.wikipedia.org/wiki/Starlink",
            "description": "Starlink is a satellite internet constellation operated by American aerospace company SpaceX",
            "agencies": [
                {
                    "response_mode": "list",
                    "id": 121,
                    "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
                    "name": "SpaceX",
                    "abbrev": "SpX",
                    "type": {
                        "id": 3,
                        "name": "Commercial"
                    }
                }
            ],
            "start_date": "2018-02-22T14:17:00Z",
            "end_date": null,
            "mission_patches": [
                {
                    "id": 7,
                    "name": "Space X Starlink Mission Patch",
                    "priority": 10,
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/mission_patch_images/space2520x252_mission_patch_20221011205756.png",
                    "agency": {
                        "response_mode": "list",
                        "id": 121,
                        "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
                        "name": "SpaceX",
                        "abbrev": "SpX",
                        "type": {
                            "id": 3,
                            "name": "Commercial"
                        }
                    },
                    "response_mode": "normal"
                }
            ],
            "type": {
                "id": 3,
                "name": "Communication Constellation"
            }
        }
    ],
    "orbital_launch_attempt_count": 7159,
    "location_launch_attempt_count": 279,
    "pad_launch_attempt_count": 221,
    "agency_launch_attempt_count": 618,
    "orbital_launch_attempt_count_year": 310,
    "location_launch_attempt_count_year": 27,
    "pad_launch_attempt_count_year": 27,
    "agency_launch_attempt_count_year": 169,
    "flightclub_url": "https://flightclub.io/result?llId=9d12fbac-53de-4517-9191-4697fbc07dd2",
    "updates": [
        {
            "id": 13309,
            "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/cosmic2520penguin_profile_20210817212020.png",
            "comment": "Updated launch weather, 40% GO at start of window (80% at end).",
            "info_url": "https://45thweathersquadron.nebula.spaceforce.mil/assets/LaunchForecasts/Falcon%209%20Starlink%206-99%20L-2%20Forecast%20-%2015%20Dec%20Launch.pdf",
            "created_by": "Cosmic_Penguin",
            "created_on": "2025-12-13T22:42:00Z"
        },
        {
            "id": 13320,
            "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/cosmic2520penguin_profile_20210817212020.png",
            "comment": "Delayed to December 16, to be officially confirmed.",
            "info_url": "https://celestrak.org/NORAD/elements/supplemental/",
            "created_by": "Cosmic_Penguin",
            "created_on": "2025-12-15T01:00:00Z"
        },
        {
            "id": 13323,
            "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/cosmic2520penguin_profile_20210817212020.png",
            "comment": "Confirmed rescheduled for December 16.",
            "info_url": "https://www.spacex.com/launches/sl-6-99",
            "created_by": "Cosmic_Penguin",
            "created_on": "2025-12-15T03:24:00Z"
        },
        {
            "id": 13338,
            "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/cosmic2520penguin_profile_20210817212020.png",
            "comment": "Now targeting Dec 17 at 12:19 UTC",
            "info_url": "https://www.spacex.com/launches/sl-6-99",
            "created_by": "Cosmic_Penguin",
            "created_on": "2025-12-15T16:27:00Z"
        },
        {
            "id": 13219,
            "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/cosmic2520penguin_profile_20210817212020.png",
            "comment": "Now targeting Dec 15 at 13:11 UTC",
            "info_url": "https://www.spacex.com/launches/sl-6-99",
            "created_by": "Cosmic_Penguin",
            "created_on": "2025-12-09T21:47:00Z"
        },
        {
            "id": 13360,
            "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/cosmic2520penguin_profile_20210817212020.png",
            "comment": "Now targeting Dec 17 at 13:42 UTC",
            "info_url": "https://www.spacex.com/launches/sl-6-99",
            "created_by": "Cosmic_Penguin",
            "created_on": "2025-12-17T00:25:00Z"
        },
        {
            "id": 13371,
            "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/ll2_profile_20230311134739.png",
            "comment": "Unofficial Re-stream by SPACE AFFAIRS has started",
            "info_url": "https://www.youtube.com/watch?v=6SGOvgqwzys",
            "created_by": "LL2",
            "created_on": "2025-12-17T13:32:00Z"
        },
        {
            "id": 13373,
            "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/ll2_profile_20230311134739.png",
            "comment": "Unofficial Re-stream by SPACE AFFAIRS has started",
            "info_url": "https://www.youtube.com/watch?v=6SGOvgqwzys",
            "created_by": "LL2",
            "created_on": "2025-12-17T13:42:00Z"
        },
        {
            "id": 13372,
            "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/cosmic2520penguin_profile_20210817212020.png",
            "comment": "Liftoff.",
            "info_url": "https://x.com/i/broadcasts/1kvJpMElqrLxE",
            "created_by": "Cosmic_Penguin",
            "created_on": "2025-12-17T13:42:00Z"
        },
        {
            "id": 13375,
            "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/cosmic2520penguin_profile_20210817212020.png",
            "comment": "Launch success.",
            "info_url": "https://x.com/SpaceX/status/2001303610487971925",
            "created_by": "Cosmic_Penguin",
            "created_on": "2025-12-17T14:50:00Z"
        },
        {
            "id": 13150,
            "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/cosmic2520penguin_profile_20210817212020.png",
            "comment": "Added launch.",
            "info_url": "https://www.spacex.com/launches/sl-6-99",
            "created_by": "Cosmic_Penguin",
            "created_on": "2025-12-05T18:54:00Z"
        }
    ],
    "info_urls": [
        {
            "priority": 10,
            "source": "spacex.com",
            "title": "SpaceX",
            "description": "SpaceX designs, manufactures and launches advanced rockets and spacecraft. The company was founded in 2002 to revolutionize space technology, with the ultimate goal of enabling people to live on other planets.",
            "feature_image": null,
            "url": "https://www.spacex.com/launches/sl-6-99",
            "type": {
                "id": 1,
                "name": "Official Page"
            },
            "language": {
                "id": 1,
                "name": "English",
                "code": "en"
            }
        }
    ],
    "vid_urls": [
        {
            "priority": 7,
            "source": "youtube.com",
            "publisher": "The Space Devs",
            "title": "SpaceX Starlink Group 6-99",
            "description": "On Wednesday December 17, 2025, SpaceX launched the Starlink Group 6-99 mission with a Falcon 9 Block 5 from Launch Complex 39A at Kennedy Space Center, FL, USA.\n\nDescription generated from the Launch Library 2 API.\n——————————————————————\nObject in the Launch Library 2 API: https://ll.thespacedevs.com/2.3.0/launches/9d12fbac-53de-4517-9191-4697fbc07dd2/?format=json\nRelated news in the Spaceflight News API: https://api.spaceflightnewsapi.net/v4/articles/?launch=9d12fbac-53de-4517-9191-4697fbc07dd2\n——————————————————————\n\nRehosted on YouTube by The Space Devs.\nSupport our work on https://www.patreon.com/TheSpaceDevs\n——————————————————————\nWebsite: https://thespacedevs.com\nX: https://x.com/thespacedevs\nDiscord: https://discord.com/invite/p7ntkNA\n——————————————————————\n\nOriginal video: ht...",
            "feature_image": "https://i.ytimg.com/vi/wqUA1Eg-QlU/hqdefault.jpg",
            "url": "https://www.youtube.com/watch?v=wqUA1Eg-QlU",
            "type": {
                "id": 4,
                "name": "Unofficial Re-stream"
            },
            "language": {
                "id": 1,
                "name": "English",
                "code": "en"
            },
            "start_time": "2025-12-17T14:01:25Z",
            "end_time": "2025-12-17T14:18:29Z",
            "live": false
        },
        {
            "priority": 8,
            "source": null,
            "publisher": "Spaceflight Now",
            "title": "Watch Live: SpaceX launches Starlink satellites on Falcon 9 rocket from NASA's Kennedy Space Center",
            "description": "Watch live coverage as SpaceX launches a Falcon 9 rocket with another 29 satellites for the company's Starlink internet service. Liftoff from Launch Complex 39A at the Kennedy Space Center is scheduled for Wednesday Dec. 17 at 8:42 a.m. EST (1342 UTC). The first-stage booster for this mission, B1094, making its sixth flight, will land on SpaceX's drone ship 'Just Read the Instructions', stationed in the Atlantic Ocean, a little more than eight minutes after leaving the launch pad.\n\nThe mission, designated Starlink 6-99, will take a south-easterly trajectory on departure from Florida's Space Coast.\n\nOur live coverage with commentary from Spaceflight Now's Will Robinson-Smith will start about one hour prior to launch.\n\nVideos like this are made possible by the support of our members. Jo...",
            "feature_image": "https://i.ytimg.com/vi/HN03q45_ffo/maxresdefault.jpg",
            "url": "https://www.youtube.com/watch?v=HN03q45_ffo",
            "type": {
                "id": 3,
                "name": "Unofficial Webcast"
            },
            "language": {
                "id": 1,
                "name": "English",
                "code": "en"
            },
            "start_time": "2025-12-17T12:42:44Z",
            "end_time": "2025-12-17T14:09:58Z",
            "live": false
        },
        {
            "priority": 9,
            "source": null,
            "publisher": "NASASpaceflight",
            "title": "SpaceX Falcon 9 launches Starlink 6-99",
            "description": "SpaceX launch of a batch of 29 Starlink v2-mini satellites for their second-generation high-speed low earth orbit internet satellite constellation.\n\nLaunch window: December 17th from 7:19AM to 11:19PM EST (12:19-16:19 UTC)\nCurrent T0: December 17th at 8:42:10AM EST (13:42:10 UTC)\n\nMission: Falcon 9 launch of 29 Starlink v2 Mini satellites into orbit.\nLaunch location: Launch Complex 39A, Kennedy Space Center, Earth.\nTarget orbit: Low-Earth Orbit (LEO); 263x274km 42.99°\nBooster: B1094-6; 41d 12h 11min turnaround\nBooster history: Starlink 12-10, Axiom-4, Crew-11, NG-23, Starlink 6-81.\nBooster recovery: Just Read The Instructions (JRTI)\nFairing recovery: Doug\nRocket trajectory: Southeast passing northeast of the Bahamas.\nPayload mass: Approximately 17 tonnes.\nStats:\n  - SpaceX's 608th lau...",
            "feature_image": "https://i.ytimg.com/vi/JD05O8b1kKM/maxresdefault.jpg",
            "url": "https://www.youtube.com/watch?v=JD05O8b1kKM",
            "type": {
                "id": 3,
                "name": "Unofficial Webcast"
            },
            "language": {
                "id": 1,
                "name": "English",
                "code": "en"
            },
            "start_time": "2025-12-17T12:42:08Z",
            "end_time": "2025-12-17T14:04:40Z",
            "live": false
        },
        {
            "priority": 10,
            "source": "x.com",
            "publisher": "SpaceX",
            "title": "Starlink Mission",
            "description": "Livestream on X",
            "feature_image": "https://pbs.twimg.com/media/G7_RCKdXoAI6N5F.jpg",
            "url": "https://x.com/i/broadcasts/1kvJpMElqrLxE",
            "type": {
                "id": 1,
                "name": "Official Webcast"
            },
            "language": {
                "id": 1,
                "name": "English",
                "code": "en"
            },
            "start_time": "2025-12-17T13:34:55Z",
            "end_time": "2025-12-17T13:52:00Z",
            "live": false
        }
    ],
    "timeline": [
        {
            "type": {
                "id": 11,
                "abbrev": "Max-Q",
                "description": "Maximum dynamic pressure"
            },
            "relative_time": "PT1M12S"
        },
        {
            "type": {
                "id": 12,
                "abbrev": "MECO",
                "description": "Cut-off of the main engine"
            },
            "relative_time": "PT2M24S"
        },
        {
            "type": {
                "id": 13,
                "abbrev": "Stage 2 Separation",
                "description": "Separation of the second stage from the first"
            },
            "relative_time": "PT2M27S"
        },
        {
            "type": {
                "id": 14,
                "abbrev": "SES-1",
                "description": "First start of the second engine"
            },
            "relative_time": "PT2M35S"
        },
        {
            "type": {
                "id": 15,
                "abbrev": "Fairing Separation",
                "description": "Separation of the payload fairing"
            },
            "relative_time": "PT2M56S"
        },
        {
            "type": {
                "id": 16,
                "abbrev": "Entry Burn Startup",
                "description": "Start of the atmospheric entry burn"
            },
            "relative_time": "PT6M7S"
        },
        {
            "type": {
                "id": 17,
                "abbrev": "Entry Burn Shutdown",
                "description": "End of the atmospheric entry burn"
            },
            "relative_time": "PT6M34S"
        },
        {
            "type": {
                "id": 18,
                "abbrev": "Stage 1 Landing Burn",
                "description": "Start of the first stage landing burn"
            },
            "relative_time": "PT7M59S"
        },
        {
            "type": {
                "id": 19,
                "abbrev": "Stage 1 Landing",
                "description": "Landing of the first stage"
            },
            "relative_time": "PT8M23S"
        },
        {
            "type": {
                "id": 20,
                "abbrev": "SECO-1",
                "description": "First cut-off of the second engine"
            },
            "relative_time": "PT8M38S"
        },
        {
            "type": {
                "id": 21,
                "abbrev": "SES-2",
                "description": "Second start of the second engine"
            },
            "relative_time": "PT53M49S"
        },
        {
            "type": {
                "id": 22,
                "abbrev": "SECO-2",
                "description": "Second cut-off of the second engine"
            },
            "relative_time": "PT53M51S"
        },
        {
            "type": {
                "id": 23,
                "abbrev": "Starlink Deployment",
                "description": "Deployment of the Starlink stack from the rocket"
            },
            "relative_time": "PT1H4M56S"
        }
    ],
    "pad_turnaround": "P8DT15H16M",
    "mission_patches": [
        {
            "id": 7,
            "name": "Space X Starlink Mission Patch",
            "priority": 10,
            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/mission_patch_images/space2520x252_mission_patch_20221011205756.png",
            "agency": {
                "response_mode": "list",
                "id": 121,
                "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
                "name": "SpaceX",
                "abbrev": "SpX",
                "type": {
                    "id": 3,
                    "name": "Commercial"
                }
            },
            "response_mode": "normal"
        }
    ]
}
    """
        val jsonEle: JsonElement = Json.parseToJsonElement(jsonString)
        val expectedResult = Launch(launchID = "9d12fbac-53de-4517-9191-4697fbc07dd2", launchURL = "https://lldev.thespacedevs.com/2.3.0/launches/9d12fbac-53de-4517-9191-4697fbc07dd2/", missionName = "Starlink Group 6-99", padName = "Launch Complex 39A", agencyID = "121", rocketID = "8839", rocketVersionID = "164", launchTimeUTC = ZonedDateTime.parse("2025-12-17T13:42:10Z"), launchTimePadLocal = ZonedDateTime.parse("2025-12-17T08:42:10-05:00[America/New_York]"), reusableParts = listOf(Reusable(reuseID = "194", reuseName = "B1094", flightNumber = 6, landingLocationAbbr = "JRTI", landingLocationName = "Just Read the Instructions", landingType = LandingType.ASDS, status = true)), success = Status.SUCCESS, windowOpen = ZonedDateTime.parse("2025-12-17T12:19Z"), windowClose = ZonedDateTime.parse("2025-12-17T16:19Z"), imageUrl = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/falcon2520925_image_20221009234147.png", imageCredit = "SpaceX", licenseType = "CC BY-NC 2.0", weatherProbability = 95.0, agencyName = "SpaceX", rocketName = "Falcon 9", rocketVariantName = "Falcon 9 Block 5", description = "A batch of 29 satellites for the Starlink mega-constellation - SpaceX's project for space-based Internet communication system.", orbit = "Low Earth Orbit", padCords = Location(latitude = 28.60822681, longitude = -80.60428186), padLocation = "Kennedy Space Center, FL, USA", padCountry = "United States of America", padTimeZone = "America/New_York", liveStreamUrl = "HOLD TEST", netPrecision = "The T-0 is accurate to the second.")
        assertThat(parseLaunch(launchJsonEle = jsonEle)).equals(expectedResult)
    }
    @Test
    fun testMultiReuse() {
        val jsonString = """
        {
    "id": "ed83366c-872c-4484-97c1-bc74832304fc",
    "url": "https://lldev.thespacedevs.com/2.3.0/launches/ed83366c-872c-4484-97c1-bc74832304fc/",
    "name": "Starship | Flight 12",
    "response_mode": "detailed",
    "slug": "starship-flight-12",
    "launch_designator": null,
    "status": {
        "id": 2,
        "name": "To Be Determined",
        "abbrev": "TBD",
        "description": "Current date is a placeholder or rough estimation based on unreliable or interpreted sources."
    },
    "last_updated": "2025-11-05T18:10:55Z",
    "net": "2026-01-31T00:00:00Z",
    "net_precision": {
        "id": 7,
        "name": "Month",
        "abbrev": "M",
        "description": "The T-0 is expected in the given month."
    },
    "window_end": "2026-01-31T00:00:00Z",
    "window_start": "2026-01-31T00:00:00Z",
    "image": {
        "id": 2301,
        "name": "Starship on the launch pad during WDR (Flight 7)",
        "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/starship_on_the_image_20250111100520.jpg",
        "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/starship_on_the_image_thumbnail_20250111100521.jpeg",
        "credit": "SpaceX",
        "license": {
            "id": 1,
            "name": "Unknown",
            "priority": 9,
            "link": null
        },
        "single_use": false,
        "variants": []
    },
    "infographic": null,
    "probability": null,
    "weather_concerns": null,
    "failreason": "",
    "hashtag": null,
    "launch_service_provider": {
        "response_mode": "normal",
        "id": 121,
        "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
        "name": "SpaceX",
        "abbrev": "SpX",
        "type": {
            "id": 3,
            "name": "Commercial"
        },
        "featured": true,
        "country": [
            {
                "id": 2,
                "name": "United States of America",
                "alpha_2_code": "US",
                "alpha_3_code": "USA",
                "nationality_name": "American",
                "nationality_name_composed": "Americano"
            }
        ],
        "description": "Space Exploration Technologies Corp., known as SpaceX, is an American aerospace manufacturer and space transport services company headquartered in Hawthorne, California. It was founded in 2002 by entrepreneur Elon Musk with the goal of reducing space transportation costs and enabling the colonization of Mars. SpaceX operates from many pads, on the East Coast of the US they operate from SLC-40 at Cape Canaveral Space Force Station and historic LC-39A at Kennedy Space Center. They also operate from SLC-4E at Vandenberg Space Force Base, California, usually for polar launches. Another launch site is being developed at Boca Chica, Texas.",
        "administrator": "CEO: Elon Musk",
        "founding_year": 2002,
        "launchers": "Falcon | Starship",
        "spacecraft": "Dragon",
        "parent": null,
        "image": {
            "id": 29,
            "name": "[AUTO] SpaceX - image",
            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_image_20190207032501.jpeg",
            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184704.jpeg",
            "credit": null,
            "license": {
                "id": 1,
                "name": "Unknown",
                "priority": 9,
                "link": null
            },
            "single_use": true,
            "variants": []
        },
        "logo": {
            "id": 186,
            "name": "[AUTO] SpaceX - logo",
            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_logo_20220826094919.png",
            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305185138.png",
            "credit": null,
            "license": {
                "id": 1,
                "name": "Unknown",
                "priority": 9,
                "link": null
            },
            "single_use": true,
            "variants": []
        },
        "social_logo": {
            "id": 94,
            "name": "[AUTO] SpaceX - social_logo",
            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_nation_20230531064544.jpg",
            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184848.jpeg",
            "credit": null,
            "license": {
                "id": 1,
                "name": "Unknown",
                "priority": 9,
                "link": null
            },
            "single_use": true,
            "variants": []
        },
        "total_launch_count": 619,
        "consecutive_successful_launches": 104,
        "successful_launches": 604,
        "failed_launches": 15,
        "pending_launches": 116,
        "consecutive_successful_landings": 103,
        "successful_landings": 564,
        "failed_landings": 27,
        "attempted_landings": 590,
        "successful_landings_spacecraft": 58,
        "failed_landings_spacecraft": 3,
        "attempted_landings_spacecraft": 61,
        "successful_landings_payload": 0,
        "failed_landings_payload": 0,
        "attempted_landings_payload": 0,
        "info_url": "https://www.spacex.com/",
        "wiki_url": "https://en.wikipedia.org/wiki/SpaceX",
        "social_media_links": []
    },
    "rocket": {
        "id": 8708,
        "configuration": {
            "response_mode": "detailed",
            "id": 522,
            "url": "https://lldev.thespacedevs.com/2.3.0/launcher_configurations/522/",
            "name": "Starship",
            "families": [
                {
                    "response_mode": "detailed",
                    "id": 156,
                    "name": "Starship",
                    "manufacturer": [
                        {
                            "response_mode": "normal",
                            "id": 121,
                            "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
                            "name": "SpaceX",
                            "abbrev": "SpX",
                            "type": {
                                "id": 3,
                                "name": "Commercial"
                            },
                            "featured": true,
                            "country": [
                                {
                                    "id": 2,
                                    "name": "United States of America",
                                    "alpha_2_code": "US",
                                    "alpha_3_code": "USA",
                                    "nationality_name": "American",
                                    "nationality_name_composed": "Americano"
                                }
                            ],
                            "description": "Space Exploration Technologies Corp., known as SpaceX, is an American aerospace manufacturer and space transport services company headquartered in Hawthorne, California. It was founded in 2002 by entrepreneur Elon Musk with the goal of reducing space transportation costs and enabling the colonization of Mars. SpaceX operates from many pads, on the East Coast of the US they operate from SLC-40 at Cape Canaveral Space Force Station and historic LC-39A at Kennedy Space Center. They also operate from SLC-4E at Vandenberg Space Force Base, California, usually for polar launches. Another launch site is being developed at Boca Chica, Texas.",
                            "administrator": "CEO: Elon Musk",
                            "founding_year": 2002,
                            "launchers": "Falcon | Starship",
                            "spacecraft": "Dragon",
                            "parent": null,
                            "image": {
                                "id": 29,
                                "name": "[AUTO] SpaceX - image",
                                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_image_20190207032501.jpeg",
                                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184704.jpeg",
                                "credit": null,
                                "license": {
                                    "id": 1,
                                    "name": "Unknown",
                                    "priority": 9,
                                    "link": null
                                },
                                "single_use": true,
                                "variants": []
                            },
                            "logo": {
                                "id": 186,
                                "name": "[AUTO] SpaceX - logo",
                                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_logo_20220826094919.png",
                                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305185138.png",
                                "credit": null,
                                "license": {
                                    "id": 1,
                                    "name": "Unknown",
                                    "priority": 9,
                                    "link": null
                                },
                                "single_use": true,
                                "variants": []
                            },
                            "social_logo": {
                                "id": 94,
                                "name": "[AUTO] SpaceX - social_logo",
                                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_nation_20230531064544.jpg",
                                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184848.jpeg",
                                "credit": null,
                                "license": {
                                    "id": 1,
                                    "name": "Unknown",
                                    "priority": 9,
                                    "link": null
                                },
                                "single_use": true,
                                "variants": []
                            },
                            "total_launch_count": 619,
                            "consecutive_successful_launches": 104,
                            "successful_launches": 604,
                            "failed_launches": 15,
                            "pending_launches": 116,
                            "consecutive_successful_landings": 103,
                            "successful_landings": 564,
                            "failed_landings": 27,
                            "attempted_landings": 590,
                            "successful_landings_spacecraft": 58,
                            "failed_landings_spacecraft": 3,
                            "attempted_landings_spacecraft": 61,
                            "successful_landings_payload": 0,
                            "failed_landings_payload": 0,
                            "attempted_landings_payload": 0,
                            "info_url": "https://www.spacex.com/",
                            "wiki_url": "https://en.wikipedia.org/wiki/SpaceX",
                            "social_media_links": []
                        }
                    ],
                    "parent": null,
                    "description": "",
                    "active": true,
                    "maiden_flight": "2019-07-26",
                    "total_launch_count": 20,
                    "consecutive_successful_launches": 2,
                    "successful_launches": 12,
                    "failed_launches": 8,
                    "pending_launches": 3,
                    "attempted_landings": 19,
                    "successful_landings": 11,
                    "failed_landings": 8,
                    "consecutive_successful_landings": 2
                }
            ],
            "full_name": "Starship V3",
            "variant": "V3",
            "active": false,
            "is_placeholder": false,
            "manufacturer": {
                "response_mode": "normal",
                "id": 121,
                "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
                "name": "SpaceX",
                "abbrev": "SpX",
                "type": {
                    "id": 3,
                    "name": "Commercial"
                },
                "featured": true,
                "country": [
                    {
                        "id": 2,
                        "name": "United States of America",
                        "alpha_2_code": "US",
                        "alpha_3_code": "USA",
                        "nationality_name": "American",
                        "nationality_name_composed": "Americano"
                    }
                ],
                "description": "Space Exploration Technologies Corp., known as SpaceX, is an American aerospace manufacturer and space transport services company headquartered in Hawthorne, California. It was founded in 2002 by entrepreneur Elon Musk with the goal of reducing space transportation costs and enabling the colonization of Mars. SpaceX operates from many pads, on the East Coast of the US they operate from SLC-40 at Cape Canaveral Space Force Station and historic LC-39A at Kennedy Space Center. They also operate from SLC-4E at Vandenberg Space Force Base, California, usually for polar launches. Another launch site is being developed at Boca Chica, Texas.",
                "administrator": "CEO: Elon Musk",
                "founding_year": 2002,
                "launchers": "Falcon | Starship",
                "spacecraft": "Dragon",
                "parent": null,
                "image": {
                    "id": 29,
                    "name": "[AUTO] SpaceX - image",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_image_20190207032501.jpeg",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184704.jpeg",
                    "credit": null,
                    "license": {
                        "id": 1,
                        "name": "Unknown",
                        "priority": 9,
                        "link": null
                    },
                    "single_use": true,
                    "variants": []
                },
                "logo": {
                    "id": 186,
                    "name": "[AUTO] SpaceX - logo",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_logo_20220826094919.png",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305185138.png",
                    "credit": null,
                    "license": {
                        "id": 1,
                        "name": "Unknown",
                        "priority": 9,
                        "link": null
                    },
                    "single_use": true,
                    "variants": []
                },
                "social_logo": {
                    "id": 94,
                    "name": "[AUTO] SpaceX - social_logo",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_nation_20230531064544.jpg",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184848.jpeg",
                    "credit": null,
                    "license": {
                        "id": 1,
                        "name": "Unknown",
                        "priority": 9,
                        "link": null
                    },
                    "single_use": true,
                    "variants": []
                },
                "total_launch_count": 619,
                "consecutive_successful_launches": 104,
                "successful_launches": 604,
                "failed_launches": 15,
                "pending_launches": 116,
                "consecutive_successful_landings": 103,
                "successful_landings": 564,
                "failed_landings": 27,
                "attempted_landings": 590,
                "successful_landings_spacecraft": 58,
                "failed_landings_spacecraft": 3,
                "attempted_landings_spacecraft": 61,
                "successful_landings_payload": 0,
                "failed_landings_payload": 0,
                "attempted_landings_payload": 0,
                "info_url": "https://www.spacex.com/",
                "wiki_url": "https://en.wikipedia.org/wiki/SpaceX",
                "social_media_links": []
            },
            "program": [
                {
                    "response_mode": "normal",
                    "id": 1,
                    "url": "https://lldev.thespacedevs.com/2.3.0/programs/1/",
                    "name": "SpaceX Starship",
                    "image": {
                        "id": 2301,
                        "name": "Starship on the launch pad during WDR (Flight 7)",
                        "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/starship_on_the_image_20250111100520.jpg",
                        "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/starship_on_the_image_thumbnail_20250111100521.jpeg",
                        "credit": "SpaceX",
                        "license": {
                            "id": 1,
                            "name": "Unknown",
                            "priority": 9,
                            "link": null
                        },
                        "single_use": false,
                        "variants": []
                    },
                    "info_url": "https://www.spacex.com/vehicles/starship/",
                    "wiki_url": "https://en.wikipedia.org/wiki/SpaceX_Starship",
                    "description": "The SpaceX Starship is a fully reusable super heavy-lift launch vehicle under development by SpaceX since 2012, as a self-funded private spaceflight project.  The second stage of the Starship — is designed as a long-duration cargo and passenger-carrying spacecraft. It is expected to be initially used without any booster stage at all, as part of an extensive development program to prove out launch-and-landing and iterate on a variety of design details, particularly with respect to the vehicle's atmospheric reentry.",
                    "agencies": [
                        {
                            "response_mode": "list",
                            "id": 121,
                            "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
                            "name": "SpaceX",
                            "abbrev": "SpX",
                            "type": {
                                "id": 3,
                                "name": "Commercial"
                            }
                        }
                    ],
                    "start_date": "2019-03-01T05:00:00Z",
                    "end_date": null,
                    "mission_patches": [],
                    "type": {
                        "id": 7,
                        "name": "Technology"
                    }
                }
            ],
            "reusable": true,
            "image": null,
            "info_url": "https://www.spacex.com/vehicles/starship",
            "wiki_url": "https://en.wikipedia.org/wiki/SpaceX_Starship#Block_3",
            "description": "Third development version of the Starship reusable two-stage super heavy-lift launch vehicle.",
            "alias": "",
            "min_stage": 2,
            "max_stage": 2,
            "length": 124.4,
            "diameter": 9.0,
            "maiden_flight": null,
            "launch_cost": null,
            "launch_mass": 5250.0,
            "leo_capacity": 100000.0,
            "gto_capacity": null,
            "geo_capacity": null,
            "sso_capacity": null,
            "to_thrust": 80807.0,
            "apogee": null,
            "total_launch_count": 0,
            "consecutive_successful_launches": 0,
            "successful_launches": 0,
            "failed_launches": 0,
            "pending_launches": 1,
            "attempted_landings": 0,
            "successful_landings": 0,
            "failed_landings": 0,
            "consecutive_successful_landings": 0,
            "fastest_turnaround": null
        },
        "launcher_stage": [
            {
                "id": 801,
                "type": "Core",
                "reused": false,
                "launcher_flight_number": 1,
                "launcher": {
                    "response_mode": "normal",
                    "id": 243,
                    "url": "https://lldev.thespacedevs.com/2.3.0/launchers/243/",
                    "flight_proven": false,
                    "serial_number": "Booster 19",
                    "is_placeholder": false,
                    "status": {
                        "id": 3,
                        "name": "active"
                    },
                    "image": null,
                    "details": "First Block 3 Super Heavy booster to fly with various enhancements including Raptor 3 engines, integrated interstage and 3 grid fins instead of 4 used. Planned for usage on the 12th Starship flight test, replacing Booster 18 written off during ground testing in November 2025.",
                    "successful_landings": 0,
                    "attempted_landings": 0,
                    "flights": 0,
                    "last_launch_date": null,
                    "first_launch_date": null,
                    "fastest_turnaround": null
                },
                "previous_flight_date": null,
                "turn_around_time": null,
                "landing": null,
                "previous_flight": null
            }
        ],
        "spacecraft_stage": [
            {
                "id": 895,
                "url": "https://lldev.thespacedevs.com/2.3.0/spacecraft_flights/895/",
                "destination": "Suborbital",
                "mission_end": null,
                "spacecraft": {
                    "response_mode": "normal",
                    "id": 621,
                    "url": "https://lldev.thespacedevs.com/2.3.0/spacecraft/621/",
                    "name": "Ship 39",
                    "serial_number": "S39",
                    "is_placeholder": false,
                    "image": {
                        "id": 2334,
                        "name": "Starship Ship 34 static fire",
                        "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/starship_ship_3_image_20250221072318.jpg",
                        "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/starship_ship_3_image_thumbnail_20250221072319.jpeg",
                        "credit": "SpaceX",
                        "license": {
                            "id": 1,
                            "name": "Unknown",
                            "priority": 9,
                            "link": null
                        },
                        "single_use": true,
                        "variants": []
                    },
                    "in_space": false,
                    "time_in_space": "P0D",
                    "time_docked": "P0D",
                    "flights_count": 0,
                    "mission_ends_count": 0,
                    "status": {
                        "id": 5,
                        "name": "Under construction"
                    },
                    "description": "First V3 Starship planned to fly on flight 12.",
                    "spacecraft_config": {
                        "response_mode": "detailed",
                        "id": 46,
                        "url": "https://lldev.thespacedevs.com/2.3.0/spacecraft_configurations/46/",
                        "name": "Starship V3",
                        "type": {
                            "id": 6,
                            "name": "Reuseable Upper Stage"
                        },
                        "agency": {
                            "response_mode": "normal",
                            "id": 121,
                            "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
                            "name": "SpaceX",
                            "abbrev": "SpX",
                            "type": {
                                "id": 3,
                                "name": "Commercial"
                            },
                            "featured": true,
                            "country": [
                                {
                                    "id": 2,
                                    "name": "United States of America",
                                    "alpha_2_code": "US",
                                    "alpha_3_code": "USA",
                                    "nationality_name": "American",
                                    "nationality_name_composed": "Americano"
                                }
                            ],
                            "description": "Space Exploration Technologies Corp., known as SpaceX, is an American aerospace manufacturer and space transport services company headquartered in Hawthorne, California. It was founded in 2002 by entrepreneur Elon Musk with the goal of reducing space transportation costs and enabling the colonization of Mars. SpaceX operates from many pads, on the East Coast of the US they operate from SLC-40 at Cape Canaveral Space Force Station and historic LC-39A at Kennedy Space Center. They also operate from SLC-4E at Vandenberg Space Force Base, California, usually for polar launches. Another launch site is being developed at Boca Chica, Texas.",
                            "administrator": "CEO: Elon Musk",
                            "founding_year": 2002,
                            "launchers": "Falcon | Starship",
                            "spacecraft": "Dragon",
                            "parent": null,
                            "image": {
                                "id": 29,
                                "name": "[AUTO] SpaceX - image",
                                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_image_20190207032501.jpeg",
                                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184704.jpeg",
                                "credit": null,
                                "license": {
                                    "id": 1,
                                    "name": "Unknown",
                                    "priority": 9,
                                    "link": null
                                },
                                "single_use": true,
                                "variants": []
                            },
                            "logo": {
                                "id": 186,
                                "name": "[AUTO] SpaceX - logo",
                                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_logo_20220826094919.png",
                                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305185138.png",
                                "credit": null,
                                "license": {
                                    "id": 1,
                                    "name": "Unknown",
                                    "priority": 9,
                                    "link": null
                                },
                                "single_use": true,
                                "variants": []
                            },
                            "social_logo": {
                                "id": 94,
                                "name": "[AUTO] SpaceX - social_logo",
                                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_nation_20230531064544.jpg",
                                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184848.jpeg",
                                "credit": null,
                                "license": {
                                    "id": 1,
                                    "name": "Unknown",
                                    "priority": 9,
                                    "link": null
                                },
                                "single_use": true,
                                "variants": []
                            }
                        },
                        "family": [
                            {
                                "response_mode": "normal",
                                "id": 8,
                                "name": "Starship",
                                "description": "SpaceX Starship upper stages family",
                                "manufacturer": {
                                    "response_mode": "normal",
                                    "id": 121,
                                    "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
                                    "name": "SpaceX",
                                    "abbrev": "SpX",
                                    "type": {
                                        "id": 3,
                                        "name": "Commercial"
                                    },
                                    "featured": true,
                                    "country": [
                                        {
                                            "id": 2,
                                            "name": "United States of America",
                                            "alpha_2_code": "US",
                                            "alpha_3_code": "USA",
                                            "nationality_name": "American",
                                            "nationality_name_composed": "Americano"
                                        }
                                    ],
                                    "description": "Space Exploration Technologies Corp., known as SpaceX, is an American aerospace manufacturer and space transport services company headquartered in Hawthorne, California. It was founded in 2002 by entrepreneur Elon Musk with the goal of reducing space transportation costs and enabling the colonization of Mars. SpaceX operates from many pads, on the East Coast of the US they operate from SLC-40 at Cape Canaveral Space Force Station and historic LC-39A at Kennedy Space Center. They also operate from SLC-4E at Vandenberg Space Force Base, California, usually for polar launches. Another launch site is being developed at Boca Chica, Texas.",
                                    "administrator": "CEO: Elon Musk",
                                    "founding_year": 2002,
                                    "launchers": "Falcon | Starship",
                                    "spacecraft": "Dragon",
                                    "parent": null,
                                    "image": {
                                        "id": 29,
                                        "name": "[AUTO] SpaceX - image",
                                        "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_image_20190207032501.jpeg",
                                        "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184704.jpeg",
                                        "credit": null,
                                        "license": {
                                            "id": 1,
                                            "name": "Unknown",
                                            "priority": 9,
                                            "link": null
                                        },
                                        "single_use": true,
                                        "variants": []
                                    },
                                    "logo": {
                                        "id": 186,
                                        "name": "[AUTO] SpaceX - logo",
                                        "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_logo_20220826094919.png",
                                        "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305185138.png",
                                        "credit": null,
                                        "license": {
                                            "id": 1,
                                            "name": "Unknown",
                                            "priority": 9,
                                            "link": null
                                        },
                                        "single_use": true,
                                        "variants": []
                                    },
                                    "social_logo": {
                                        "id": 94,
                                        "name": "[AUTO] SpaceX - social_logo",
                                        "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_nation_20230531064544.jpg",
                                        "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184848.jpeg",
                                        "credit": null,
                                        "license": {
                                            "id": 1,
                                            "name": "Unknown",
                                            "priority": 9,
                                            "link": null
                                        },
                                        "single_use": true,
                                        "variants": []
                                    }
                                },
                                "parent": null,
                                "maiden_flight": "2023-04-20",
                                "spacecraft_flown": 11,
                                "total_launch_count": 11,
                                "successful_launches": 6,
                                "failed_launches": 5,
                                "attempted_landings": 8,
                                "successful_landings": 5,
                                "failed_landings": 3
                            }
                        ],
                        "in_use": true,
                        "image": {
                            "id": 2438,
                            "name": "Starship during stacking operations (Flight 10)",
                            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/starship_during_image_20250824175325.jpg",
                            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/starship_during_image_thumbnail_20250824175326.jpeg",
                            "credit": "SpaceX",
                            "license": {
                                "id": 1,
                                "name": "Unknown",
                                "priority": 9,
                                "link": null
                            },
                            "single_use": true,
                            "variants": []
                        },
                        "capability": "More than 100 tons to Earth orbit",
                        "history": "The third-generation Starship upper stage will be introduced on flight 12.",
                        "details": "Third-generation second stage of the two-stage Starship super heavy-lift launch vehicle.",
                        "maiden_flight": null,
                        "height": 52.1,
                        "diameter": 9.0,
                        "human_rated": false,
                        "crew_capacity": null,
                        "payload_capacity": null,
                        "payload_return_capacity": null,
                        "flight_life": "Unknown at this stage",
                        "wiki_link": "",
                        "info_link": "https://www.spacex.com/vehicles/starship",
                        "spacecraft_flown": 0,
                        "total_launch_count": 0,
                        "successful_launches": 0,
                        "failed_launches": 0,
                        "attempted_landings": 0,
                        "successful_landings": 0,
                        "failed_landings": 0,
                        "fastest_turnaround": null
                    },
                    "fastest_turnaround": null
                },
                "landing": null,
                "duration": null,
                "turn_around_time": null,
                "response_mode": "detailed",
                "launch_crew": [],
                "onboard_crew": [],
                "landing_crew": [],
                "docking_events": []
            }
        ],
        "payloads": []
    },
    "mission": {
        "id": 7301,
        "name": "Flight 12",
        "type": "Test Flight",
        "description": "12th test flight of the two-stage Starship launch vehicle. Maiden Flight of Starship V3",
        "image": null,
        "orbit": {
            "id": 15,
            "name": "Suborbital",
            "abbrev": "Sub",
            "celestial_body": {
                "response_mode": "list",
                "id": 1,
                "name": "Earth"
            }
        },
        "agencies": [
            {
                "response_mode": "normal",
                "id": 121,
                "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
                "name": "SpaceX",
                "abbrev": "SpX",
                "type": {
                    "id": 3,
                    "name": "Commercial"
                },
                "featured": true,
                "country": [
                    {
                        "id": 2,
                        "name": "United States of America",
                        "alpha_2_code": "US",
                        "alpha_3_code": "USA",
                        "nationality_name": "American",
                        "nationality_name_composed": "Americano"
                    }
                ],
                "description": "Space Exploration Technologies Corp., known as SpaceX, is an American aerospace manufacturer and space transport services company headquartered in Hawthorne, California. It was founded in 2002 by entrepreneur Elon Musk with the goal of reducing space transportation costs and enabling the colonization of Mars. SpaceX operates from many pads, on the East Coast of the US they operate from SLC-40 at Cape Canaveral Space Force Station and historic LC-39A at Kennedy Space Center. They also operate from SLC-4E at Vandenberg Space Force Base, California, usually for polar launches. Another launch site is being developed at Boca Chica, Texas.",
                "administrator": "CEO: Elon Musk",
                "founding_year": 2002,
                "launchers": "Falcon | Starship",
                "spacecraft": "Dragon",
                "parent": null,
                "image": {
                    "id": 29,
                    "name": "[AUTO] SpaceX - image",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_image_20190207032501.jpeg",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184704.jpeg",
                    "credit": null,
                    "license": {
                        "id": 1,
                        "name": "Unknown",
                        "priority": 9,
                        "link": null
                    },
                    "single_use": true,
                    "variants": []
                },
                "logo": {
                    "id": 186,
                    "name": "[AUTO] SpaceX - logo",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_logo_20220826094919.png",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305185138.png",
                    "credit": null,
                    "license": {
                        "id": 1,
                        "name": "Unknown",
                        "priority": 9,
                        "link": null
                    },
                    "single_use": true,
                    "variants": []
                },
                "social_logo": {
                    "id": 94,
                    "name": "[AUTO] SpaceX - social_logo",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_nation_20230531064544.jpg",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184848.jpeg",
                    "credit": null,
                    "license": {
                        "id": 1,
                        "name": "Unknown",
                        "priority": 9,
                        "link": null
                    },
                    "single_use": true,
                    "variants": []
                },
                "total_launch_count": 619,
                "consecutive_successful_launches": 104,
                "successful_launches": 604,
                "failed_launches": 15,
                "pending_launches": 116,
                "consecutive_successful_landings": 103,
                "successful_landings": 564,
                "failed_landings": 27,
                "attempted_landings": 590,
                "successful_landings_spacecraft": 58,
                "failed_landings_spacecraft": 3,
                "attempted_landings_spacecraft": 61,
                "successful_landings_payload": 0,
                "failed_landings_payload": 0,
                "attempted_landings_payload": 0,
                "info_url": "https://www.spacex.com/",
                "wiki_url": "https://en.wikipedia.org/wiki/SpaceX",
                "social_media_links": []
            }
        ],
        "info_urls": [],
        "vid_urls": []
    },
    "pad": {
        "id": 235,
        "url": "https://lldev.thespacedevs.com/2.3.0/pads/235/",
        "active": true,
        "agencies": [
            {
                "response_mode": "normal",
                "id": 121,
                "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
                "name": "SpaceX",
                "abbrev": "SpX",
                "type": {
                    "id": 3,
                    "name": "Commercial"
                },
                "featured": true,
                "country": [
                    {
                        "id": 2,
                        "name": "United States of America",
                        "alpha_2_code": "US",
                        "alpha_3_code": "USA",
                        "nationality_name": "American",
                        "nationality_name_composed": "Americano"
                    }
                ],
                "description": "Space Exploration Technologies Corp., known as SpaceX, is an American aerospace manufacturer and space transport services company headquartered in Hawthorne, California. It was founded in 2002 by entrepreneur Elon Musk with the goal of reducing space transportation costs and enabling the colonization of Mars. SpaceX operates from many pads, on the East Coast of the US they operate from SLC-40 at Cape Canaveral Space Force Station and historic LC-39A at Kennedy Space Center. They also operate from SLC-4E at Vandenberg Space Force Base, California, usually for polar launches. Another launch site is being developed at Boca Chica, Texas.",
                "administrator": "CEO: Elon Musk",
                "founding_year": 2002,
                "launchers": "Falcon | Starship",
                "spacecraft": "Dragon",
                "parent": null,
                "image": {
                    "id": 29,
                    "name": "[AUTO] SpaceX - image",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_image_20190207032501.jpeg",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184704.jpeg",
                    "credit": null,
                    "license": {
                        "id": 1,
                        "name": "Unknown",
                        "priority": 9,
                        "link": null
                    },
                    "single_use": true,
                    "variants": []
                },
                "logo": {
                    "id": 186,
                    "name": "[AUTO] SpaceX - logo",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_logo_20220826094919.png",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305185138.png",
                    "credit": null,
                    "license": {
                        "id": 1,
                        "name": "Unknown",
                        "priority": 9,
                        "link": null
                    },
                    "single_use": true,
                    "variants": []
                },
                "social_logo": {
                    "id": 94,
                    "name": "[AUTO] SpaceX - social_logo",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_nation_20230531064544.jpg",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184848.jpeg",
                    "credit": null,
                    "license": {
                        "id": 1,
                        "name": "Unknown",
                        "priority": 9,
                        "link": null
                    },
                    "single_use": true,
                    "variants": []
                }
            }
        ],
        "name": "Orbital Launch Pad 2",
        "image": null,
        "description": "The second launch and landing pad of the full version of the combined SpaceX Starship and Superheavy booster. To be first used for launch in 2025 with version 3 of Starship and the Superheavy booster.\r\n\r\nThe launch mount itself is named Orbital Launch Mount B.",
        "info_url": null,
        "wiki_url": "https://en.wikipedia.org/wiki/SpaceX_Starbase#Launch_site_(Orbital_Launch_Mount_(Pad_1_and_2))",
        "map_url": "https://www.google.com/maps?q=25.996603,-97.158251",
        "latitude": 25.996603,
        "longitude": -97.158251,
        "country": {
            "id": 2,
            "name": "United States of America",
            "alpha_2_code": "US",
            "alpha_3_code": "USA",
            "nationality_name": "American",
            "nationality_name_composed": "Americano"
        },
        "map_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/map_images/pad_orbital_launch_mount_b_20241126031254.jpg",
        "total_launch_count": 0,
        "orbital_launch_attempt_count": 0,
        "fastest_turnaround": null,
        "location": {
            "response_mode": "normal",
            "id": 143,
            "url": "https://lldev.thespacedevs.com/2.3.0/locations/143/",
            "name": "SpaceX Starbase, TX, USA",
            "celestial_body": {
                "response_mode": "normal",
                "id": 1,
                "name": "Earth",
                "type": {
                    "id": 1,
                    "name": "Planet"
                },
                "diameter": 12742000.0,
                "mass": 5.972168e+24,
                "gravity": 9.80655,
                "length_of_day": "1 00:00:00",
                "atmosphere": true,
                "image": {
                    "id": 2040,
                    "name": "Earth (Apollo 17)",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/earth_2528apol_image_20240402194304.jpeg",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/earth_2528apol_image_thumbnail_20240402194305.jpeg",
                    "credit": "NASA",
                    "license": {
                        "id": 4,
                        "name": "NASA Image and Media Guidelines",
                        "priority": 0,
                        "link": "https://www.nasa.gov/nasa-brand-center/images-and-media/"
                    },
                    "single_use": true,
                    "variants": []
                },
                "description": "Earth is the third planet from the Sun and the only astronomical object known to harbor life.",
                "wiki_url": "https://en.wikipedia.org/wiki/Earth",
                "total_attempted_launches": 7388,
                "successful_launches": 6838,
                "failed_launches": 550,
                "total_attempted_landings": 1243,
                "successful_landings": 1196,
                "failed_landings": 47
            },
            "active": true,
            "country": {
                "id": 2,
                "name": "United States of America",
                "alpha_2_code": "US",
                "alpha_3_code": "USA",
                "nationality_name": "American",
                "nationality_name_composed": "Americano"
            },
            "description": "Starbase is an industrial complex for Starship rockets and the headquarters of the American aerospace manufacturer company SpaceX. Located near Brownsville, Texas, United States, it has been under construction since the late 2010s by SpaceX. Starbase is composed of a spaceport near the Gulf of Mexico, a production facility at the Boca Chica village, and a small structure test site along the Texas State Highway 4.",
            "image": {
                "id": 2219,
                "name": "SpaceX Starbase in March 2022",
                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_starbase_image_20240919170357.jpg",
                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/spacex_starbase_image_thumbnail_20240919170357.jpeg",
                "credit": "SpaceX",
                "license": {
                    "id": 5,
                    "name": "CC BY-NC 2.0",
                    "priority": 1,
                    "link": "https://creativecommons.org/licenses/by-nc/2.0/"
                },
                "single_use": true,
                "variants": []
            },
            "map_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/map_images/location_143_20200803142438.jpg",
            "longitude": -97.157,
            "latitude": 25.997,
            "timezone_name": "America/Chicago",
            "total_launch_count": 20,
            "total_landing_count": 13
        }
    },
    "webcast_live": false,
    "program": [
        {
            "response_mode": "normal",
            "id": 1,
            "url": "https://lldev.thespacedevs.com/2.3.0/programs/1/",
            "name": "SpaceX Starship",
            "image": {
                "id": 2301,
                "name": "Starship on the launch pad during WDR (Flight 7)",
                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/starship_on_the_image_20250111100520.jpg",
                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/starship_on_the_image_thumbnail_20250111100521.jpeg",
                "credit": "SpaceX",
                "license": {
                    "id": 1,
                    "name": "Unknown",
                    "priority": 9,
                    "link": null
                },
                "single_use": false,
                "variants": []
            },
            "info_url": "https://www.spacex.com/vehicles/starship/",
            "wiki_url": "https://en.wikipedia.org/wiki/SpaceX_Starship",
            "description": "The SpaceX Starship is a fully reusable super heavy-lift launch vehicle under development by SpaceX since 2012, as a self-funded private spaceflight project.  The second stage of the Starship — is designed as a long-duration cargo and passenger-carrying spacecraft. It is expected to be initially used without any booster stage at all, as part of an extensive development program to prove out launch-and-landing and iterate on a variety of design details, particularly with respect to the vehicle's atmospheric reentry.",
            "agencies": [
                {
                    "response_mode": "list",
                    "id": 121,
                    "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
                    "name": "SpaceX",
                    "abbrev": "SpX",
                    "type": {
                        "id": 3,
                        "name": "Commercial"
                    }
                }
            ],
            "start_date": "2019-03-01T05:00:00Z",
            "end_date": null,
            "mission_patches": [],
            "type": {
                "id": 7,
                "name": "Technology"
            }
        }
    ],
    "orbital_launch_attempt_count": null,
    "location_launch_attempt_count": 21,
    "pad_launch_attempt_count": 1,
    "agency_launch_attempt_count": 625,
    "orbital_launch_attempt_count_year": null,
    "location_launch_attempt_count_year": 1,
    "pad_launch_attempt_count_year": 1,
    "agency_launch_attempt_count_year": 5,
    "flightclub_url": null,
    "updates": [
        {
            "id": 12766,
            "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/cosmic2520penguin_profile_20210817212020.png",
            "comment": "NET January.",
            "info_url": "https://x.com/jeff_foust/status/1986086826600186264",
            "created_by": "Cosmic_Penguin",
            "created_on": "2025-11-05T18:10:00Z"
        },
        {
            "id": 12173,
            "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/lucas2520stauder_profile_20230526163835.jpg",
            "comment": "Added launch",
            "info_url": "https://x.com/elonmusk/status/1960816999371825302",
            "created_by": "hitura-nobad",
            "created_on": "2025-09-10T08:21:00Z"
        }
    ],
    "info_urls": [],
    "vid_urls": [],
    "timeline": [],
    "pad_turnaround": "P0D",
    "mission_patches": [
        {
            "id": 773,
            "name": "Starship IFT patch",
            "priority": 10,
            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/mission_patch_images/starship2520if_mission_patch_20230414221655.png",
            "agency": {
                "response_mode": "list",
                "id": 121,
                "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
                "name": "SpaceX",
                "abbrev": "SpX",
                "type": {
                    "id": 3,
                    "name": "Commercial"
                }
            },
            "response_mode": "normal"
        }
    ]
}
    """
        val jsonEle: JsonElement = Json.parseToJsonElement(jsonString)
        val expectedResult = Launch(launchID = "ed83366c-872c-4484-97c1-bc74832304fc", launchURL = "https://lldev.thespacedevs.com/2.3.0/launches/ed83366c-872c-4484-97c1-bc74832304fc/", missionName = "Flight 12", padName = "Orbital Launch Pad 2", agencyID = "121", rocketID = "8708", rocketVersionID = "522", launchTimeUTC = ZonedDateTime.parse("2026-01-31T00:00Z"), launchTimePadLocal = ZonedDateTime.parse("2026-01-30T18:00-06:00[America/Chicago]"), reusableParts = listOf(Reusable(reuseID = "243", reuseName = "Booster 19", flightNumber = 0, landingLocationAbbr = null, landingLocationName = null, landingType = LandingType.UNKNOWN, status = null), Reusable(reuseID = "621", reuseName = "Ship 39", flightNumber = 0, landingLocationAbbr = null, landingLocationName = null, landingType = LandingType.UNKNOWN, status = null)), success = Status.TBD, windowOpen = ZonedDateTime.parse("2026-01-31T00:00Z"), windowClose = ZonedDateTime.parse("2026-01-31T00:00Z"), imageUrl = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/starship_on_the_image_20250111100520.jpg", imageCredit = "SpaceX", licenseType = "Unknown", weatherProbability = null, agencyName = "SpaceX", rocketName = "Starship", rocketVariantName = "Starship V3", description = "12th test flight of the two-stage Starship launch vehicle. Maiden Flight of Starship V3", orbit = "Suborbital", padCords = Location(latitude = 25.996603, longitude = -97.158251), padLocation = "SpaceX Starbase, TX, USA", padCountry = "United States of America", padTimeZone = "America/Chicago", liveStreamUrl = "HOLD TEST", netPrecision = "The T-0 is expected in the given month.")
        assertThat(parseLaunch(launchJsonEle = jsonEle)).equals(expectedResult)
    }

}
