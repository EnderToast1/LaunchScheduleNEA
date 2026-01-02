package com.niaw.launchnea.networking

import com.niaw.launchnea.database.entities.Launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

suspend fun updateLaunchData(){
    //Past launches
    val lastCallForPast= LocalDateTime.of(2025,12,23,0,0)     //Need to implement finding last call date for past
    lastCallForPast.minusDays(1)
    val lastCallForPastISO=lastCallForPast.toString()
    val pastLaunchesJsonArray=APIFetchRequest("https://lldev.thespacedevs.com/2.3.0/launches/previous/?mode=detailed&limit=2&last_updated__lte=$lastCallForPastISO").data

    pastLaunchesJsonArray?.forEach { launchJson ->
        //val launch=parseLaunch(launchJson)
        println(launchJson.jsonObject)

    }

}
/*"id": "a5f8ab2c-6a2c-4b83-a866-e6cd5bf63545",
            "url": "https://lldev.thespacedevs.com/2.3.0/launches/a5f8ab2c-6a2c-4b83-a866-e6cd5bf63545/",
            "name": "Falcon 9 Block 5 | Starlink Group 15-11",
            "response_mode": "detailed",
            "slug": "falcon-9-block-5-starlink-group-15-11",
            "launch_designator": "2025-293",
            "status": {
                "id": 3,
                "name": "Launch Successful",
                "abbrev": "Success",
                "description": "The launch vehicle successfully inserted its payload(s) into the target orbit(s)."
            },
            "last_updated": "2025-12-12T14:52:38Z",
            "net": "2025-12-10T11:40:30Z",
            "net_precision": {
                "id": 0,
                "name": "Second",
                "abbrev": "SEC",
                "description": "The T-0 is accurate to the second."
            },
            "window_end": "2025-12-10T12:54:00Z",
            "window_start": "2025-12-10T08:54:00Z",
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
                "id": 8815,
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
                        "id": 849,
                        "type": "Core",
                        "reused": true,
                        "launcher_flight_number": 18,
                        "launcher": {
                            "response_mode": "normal",
                            "id": 125,
                            "url": "https://lldev.thespacedevs.com/2.3.0/launchers/125/",
                            "flight_proven": true,
                            "serial_number": "B1082",
                            "is_placeholder": false,
                            "status": {
                                "id": 3,
                                "name": "active"
                            },
                            "image": {
                                "id": 2632,
                                "name": "B1082 landing at LZ-4 (USSF-62)",
                                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/b1082_landing_a_image_20251219191159.jpg",
                                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/b1082_landing_a_image_thumbnail_20251219191159.jpeg",
                                "credit": "USSF SSC",
                                "license": {
                                    "id": 17,
                                    "name": "Department of Defense Image Use Policy",
                                    "priority": 6,
                                    "link": "https://www.dimoc.mil/resources/limitations/"
                                },
                                "single_use": true,
                                "variants": []
                            },
                            "details": "Falcon 9 Block 5 booster.",
                            "successful_landings": 18,
                            "attempted_landings": 18,
                            "flights": 18,
                            "last_launch_date": "2025-12-10T11:40:30Z",
                            "first_launch_date": "2024-01-03T03:44:20Z",
                            "fastest_turnaround": "P25DT10M"
                        },
                        "previous_flight_date": "2025-10-28T00:43:49Z",
                        "turn_around_time": "P43DT10H56M41S",
                        "landing": {
                            "id": 1744,
                            "url": "https://lldev.thespacedevs.com/2.3.0/landings/1744/",
                            "attempt": true,
                            "success": true,
                            "description": "The Falcon 9 first stage B1082 has landed on ASDS OCISLY after its 18th flight.",
                            "downrange_distance": 574.0,
                            "landing_location": {
                                "id": 1,
                                "name": "Of Course I Still Love You",
                                "active": true,
                                "abbrev": "OCISLY",
                                "description": "The second ASDS barge, Of Course I Still Love You (OCISLY) services launches in the Pacific Ocean and was the site of the first landing of a SpaceX Falcon 9 first stage during CRS-8, the launch of a Dragon spacecraft to the International Space Station.",
                                "location": null,
                                "longitude": null,
                                "latitude": null,
                                "image": {
                                    "id": 1969,
                                    "name": "OCISLY empty",
                                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/ocisly_empty_image_20240310164323.png",
                                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/ocisly_empty_image_thumbnail_20240310164323.png",
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
                                "successful_landings": 172,
                                "attempted_landings": 180,
                                "failed_landings": 8,
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
                            "id": "22eb6520-2a51-4f60-b1b8-41a6bf711768",
                            "url": "https://lldev.thespacedevs.com/2.3.0/launches/22eb6520-2a51-4f60-b1b8-41a6bf711768/",
                            "name": "Falcon 9 Block 5 | Starlink Group 11-21"
                        }
                    }
                ],
                "spacecraft_stage": [],
                "payloads": []
            },
            "mission": {
                "id": 7406,
                "name": "Starlink Group 15-11",
                "type": "Communications",
                "description": "A batch of 27 satellites for the Starlink mega-constellation - SpaceX's project for space-based Internet communication system.",
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
                "id": 16,
                "url": "https://lldev.thespacedevs.com/2.3.0/pads/16/",
                "active": true,
                "agencies": [],
                "name": "Space Launch Complex 4E",
                "image": {
                    "id": 1342,
                    "name": "Falcon 9 Block 5 | SARah 2 & 3 from SLC-4E",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/falcon2520925_image_20231223073520.jpeg",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305192449.jpeg",
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
                "description": "Space Launch Complex 4 East (SLC-4E) is a launch site at Vandenberg Space Force Base, California, U.S.\r\n\r\nThe pad was previously used by Atlas and Titan rockets between 1963 and 2005. The pad was built for use by Atlas-Agena rockets, but was later rebuilt to handle Titan rockets.",
                "info_url": null,
                "wiki_url": "https://en.wikipedia.org/wiki/Vandenberg_Space_Launch_Complex_4#SLC-4E",
                "map_url": "https://www.google.com/maps?q=34.632,-120.611",
                "latitude": 34.632,
                "longitude": -120.611,
                "country": {
                    "id": 2,
                    "name": "United States of America",
                    "alpha_2_code": "US",
                    "alpha_3_code": "USA",
                    "nationality_name": "American",
                    "nationality_name_composed": "Americano"
                },
                "map_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/map_images/pad_16_20200803143532.jpg",
                "total_launch_count": 238,
                "orbital_launch_attempt_count": 238,
                "fastest_turnaround": "P2DT10H22M59S",
                "location": {
                    "response_mode": "normal",
                    "id": 11,
                    "url": "https://lldev.thespacedevs.com/2.3.0/locations/11/",
                    "name": "Vandenberg SFB, CA, USA",
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
                    "description": "Vandenberg Space Force Base is a United States Space Force Base in Santa Barbara County, California. Established in 1941, Vandenberg Space Force Base is a space launch base, launching spacecraft from the Western Range, and also performs missile testing. The United States Space Force's Space Launch Delta 30 serves as the host delta for the base, equivalent to an Air Force air base wing. In addition to its military space launch mission, Vandenberg Space Force Base also hosts space launches for civil and commercial space entities, such as NASA and SpaceX.",
                    "image": {
                        "id": 2226,
                        "name": "Vandenberg SFB imaged by Sentinel-2",
                        "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/vandenberg_sfb__image_20240920082910.jpeg",
                        "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/vandenberg_sfb__image_thumbnail_20240920082910.jpeg",
                        "credit": "Contains modified Copernicus Sentinel data 2020",
                        "license": {
                            "id": 33,
                            "name": "Copernicus Image Use Policy",
                            "priority": 0,
                            "link": "https://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32013R1159"
                        },
                        "single_use": true,
                        "variants": []
                    },
                    "map_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/map_images/location_11_20200803142416.jpg",
                    "longitude": -120.52023,
                    "latitude": 34.75133,
                    "timezone_name": "America/Los_Angeles",
                    "total_launch_count": 852,
                    "total_landing_count": 30
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
            "orbital_launch_attempt_count": 7149,
            "location_launch_attempt_count": 850,
            "pad_launch_attempt_count": 236,
            "agency_launch_attempt_count": 614,
            "orbital_launch_attempt_count_year": 300,
            "location_launch_attempt_count_year": 64,
            "pad_launch_attempt_count_year": 62,
            "agency_launch_attempt_count_year": 165,
            "flightclub_url": "https://flightclub.io/result?llId=a5f8ab2c-6a2c-4b83-a866-e6cd5bf63545",
            "updates": [
                {
                    "id": 13028,
                    "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/cosmic2520penguin_profile_20210817212020.png",
                    "comment": "Added launch.",
                    "info_url": "https://www.spacex.com/launches/sl-15-11",
                    "created_by": "Cosmic_Penguin",
                    "created_on": "2025-11-24T19:28:00Z"
                },
                {
                    "id": 13204,
                    "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/cosmic2520penguin_profile_20210817212020.png",
                    "comment": "Now targeting Dec 10 at 09:02 UTC",
                    "info_url": "https://www.spacex.com/launches/sl-15-11",
                    "created_by": "Cosmic_Penguin",
                    "created_on": "2025-12-09T15:56:00Z"
                },
                {
                    "id": 13212,
                    "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/cosmic2520penguin_profile_20210817212020.png",
                    "comment": "Now targeting Dec 10 at 11:40 UTC",
                    "info_url": "https://www.spacex.com/launches/sl-15-11",
                    "created_by": "Cosmic_Penguin",
                    "created_on": "2025-12-09T18:20:00Z"
                },
                {
                    "id": 13056,
                    "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/cosmic2520penguin_profile_20210817212020.png",
                    "comment": "Now targeting NET December 10.",
                    "info_url": "https://www.spacex.com/launches/sl-15-11",
                    "created_by": "Cosmic_Penguin",
                    "created_on": "2025-11-26T19:23:00Z"
                },
                {
                    "id": 13058,
                    "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/cosmic2520penguin_profile_20210817212020.png",
                    "comment": "Updated tentative launch window.",
                    "info_url": "https://x.com/i/broadcasts/1ypJdqNMyVpxW",
                    "created_by": "Cosmic_Penguin",
                    "created_on": "2025-11-26T19:33:00Z"
                },
                {
                    "id": 13230,
                    "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/ll2_profile_20230311134739.png",
                    "comment": "Unofficial Re-stream by SPACE AFFAIRS has started",
                    "info_url": "https://www.youtube.com/watch?v=y7m3YS5qJUY",
                    "created_by": "LL2",
                    "created_on": "2025-12-10T11:30:00Z"
                },
                {
                    "id": 13231,
                    "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/lucas2520stauder_profile_20230526163835.jpg",
                    "comment": "Liftoff!",
                    "info_url": "https://x.com/i/broadcasts/1ypJdqNMyVpxW",
                    "created_by": "hitura-nobad",
                    "created_on": "2025-12-10T11:41:00Z"
                },
                {
                    "id": 13232,
                    "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/cosmic2520penguin_profile_20210817212020.png",
                    "comment": "Launch success.",
                    "info_url": "https://x.com/SpaceX/status/1998735904475680971",
                    "created_by": "Cosmic_Penguin",
                    "created_on": "2025-12-10T12:47:00Z"
                },
                {
                    "id": 13078,
                    "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/cosmic2520penguin_profile_20210817212020.png",
                    "comment": "Tweaked launch window.",
                    "info_url": "https://www.spacex.com/launches/sl-15-11",
                    "created_by": "Cosmic_Penguin",
                    "created_on": "2025-11-30T13:32:00Z"
                }
            ],
            "info_urls": [
                {
                    "priority": 10,
                    "source": "spacex.com",
                    "title": "SpaceX",
                    "description": "SpaceX designs, manufactures and launches advanced rockets and spacecraft. The company was founded in 2002 to revolutionize space technology, with the ultimate goal of enabling people to live on other planets.",
                    "feature_image": null,
                    "url": "https://www.spacex.com/launches/sl-15-11",
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
                    "priority": 8,
                    "source": "youtube.com",
                    "publisher": "The Space Devs",
                    "title": "SpaceX Starlink Group 15-11",
                    "description": "On Wednesday December 10, 2025, SpaceX launched the Starlink Group 15-11 mission with a Falcon 9 Block 5 from Space Launch Complex 4E at Vandenberg SFB, CA, USA.\n\nDescription generated from the Launch Library 2 API.\n——————————————————————\nObject in the Launch Library 2 API: https://ll.thespacedevs.com/2.3.0/launches/a5f8ab2c-6a2c-4b83-a866-e6cd5bf63545/?format=json\nRelated news in the Spaceflight News API: https://api.spaceflightnewsapi.net/v4/articles/?launch=a5f8ab2c-6a2c-4b83-a866-e6cd5bf63545\n——————————————————————\n\nRehosted on YouTube by The Space Devs.\nSupport our work on https://www.patreon.com/TheSpaceDevs\n——————————————————————\nWebsite: https://thespacedevs.com\nX: https://x.com/thespacedevs\nDiscord: https://discord.com/invite/p7ntkNA\n——————————————————————\n\nOriginal video: ht...",
                    "feature_image": "https://i.ytimg.com/vi/i5Xesolu0sc/hqdefault.jpg",
                    "url": "https://www.youtube.com/watch?v=i5Xesolu0sc",
                    "type": {
                        "id": 4,
                        "name": "Unofficial Re-stream"
                    },
                    "language": {
                        "id": 1,
                        "name": "English",
                        "code": "en"
                    },
                    "start_time": "2025-12-10T11:59:54Z",
                    "end_time": "2025-12-10T12:17:22Z",
                    "live": false
                },
                {
                    "priority": 9,
                    "source": "youtube.com",
                    "publisher": "Spaceflight Now",
                    "title": "Watch Live: SpaceX launches Starlink internet satellites from California on Falcon 9 rocket",
                    "description": "Watch live coverage as SpaceX launches a Falcon 9 rocket from California with 27 Starlink v2 mini internet relay satellites. Liftoff from Space Launch Complex 4E (SLC-4E) at Vandenberg Space Force Base is scheduled for Wednesday, Dec. 10, at 3:40 a.m. PST (6:40 a.m. EST / 1140 UTC).\n\nThe first-stage booster, tail number B1082, making its 18th, will land on the drone ship 'Of Course I Still Love You' nearly 8.5 minutes into the flight.\n\nThe Starlink 15-11 mission trajectory will follow the coast of California, following a south-easterly path upon departure from Vandenberg.\n\nLive commentary from Spaceflight Now's Will Robinson-Smith will be provided starting about 30 minutes prior to launch.\n\nVideos like this are made possible by the support of our members. Join this channel to get acce...",
                    "feature_image": "https://i.ytimg.com/vi/HrYMzZoL5RA/maxresdefault.jpg",
                    "url": "https://www.youtube.com/watch?v=HrYMzZoL5RA",
                    "type": {
                        "id": 3,
                        "name": "Unofficial Webcast"
                    },
                    "language": {
                        "id": 1,
                        "name": "English",
                        "code": "en"
                    },
                    "start_time": "2025-12-10T11:10:49Z",
                    "end_time": "2025-12-10T11:54:21Z",
                    "live": false
                },
                {
                    "priority": 10,
                    "source": "x.com",
                    "publisher": "SpaceX",
                    "title": "Starlink Mission",
                    "description": "Livestream on X",
                    "feature_image": "https://pbs.twimg.com/media/G6irAUlW4AAIqUY.jpg",
                    "url": "https://x.com/i/broadcasts/1ypJdqNMyVpxW",
                    "type": {
                        "id": 1,
                        "name": "Official Webcast"
                    },
                    "language": {
                        "id": 1,
                        "name": "English",
                        "code": "en"
                    },
                    "start_time": "2025-12-10T11:32:55Z",
                    "end_time": "2025-12-10T11:50:24Z",
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
                    "relative_time": "PT2M25S"
                },
                {
                    "type": {
                        "id": 13,
                        "abbrev": "Stage 2 Separation",
                        "description": "Separation of the second stage from the first"
                    },
                    "relative_time": "PT2M28S"
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
                    "relative_time": "PT2M54S"
                },
                {
                    "type": {
                        "id": 16,
                        "abbrev": "Entry Burn Startup",
                        "description": "Start of the atmospheric entry burn"
                    },
                    "relative_time": "PT6M10S"
                },
                {
                    "type": {
                        "id": 17,
                        "abbrev": "Entry Burn Shutdown",
                        "description": "End of the atmospheric entry burn"
                    },
                    "relative_time": "PT6M33S"
                },
                {
                    "type": {
                        "id": 18,
                        "abbrev": "Stage 1 Landing Burn",
                        "description": "Start of the first stage landing burn"
                    },
                    "relative_time": "PT8M"
                },
                {
                    "type": {
                        "id": 19,
                        "abbrev": "Stage 1 Landing",
                        "description": "Landing of the first stage"
                    },
                    "relative_time": "PT8M24S"
                },
                {
                    "type": {
                        "id": 20,
                        "abbrev": "SECO-1",
                        "description": "First cut-off of the second engine"
                    },
                    "relative_time": "PT8M39S"
                },
                {
                    "type": {
                        "id": 21,
                        "abbrev": "SES-2",
                        "description": "Second start of the second engine"
                    },
                    "relative_time": "PT52M24S"
                },
                {
                    "type": {
                        "id": 22,
                        "abbrev": "SECO-2",
                        "description": "Second cut-off of the second engine"
                    },
                    "relative_time": "PT52M26S"
                },
                {
                    "type": {
                        "id": 23,
                        "abbrev": "Starlink Deployment",
                        "description": "Deployment of the Starlink stack from the rocket"
                    },
                    "relative_time": "PT1H1M16S"
                }
            ],
            "pad_turnaround": "P2DT17H42M",
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

*/
/*
suspend fun parseLaunch(launchJsonEle: JsonElement): Launch? {
    val launchJsonObj=launchJsonEle.jsonObject
    val id= launchJsonObj["id"]?.jsonPrimitive?.content ?: return null
    val missionName= launchJsonObj["mission"]?.jsonObject?.get("name")?.jsonPrimitive?.content
    val padName= launchJsonObj["pad"]?.jsonObject?.get("name")?.jsonPrimitive?.content
    val agencyID= launchJsonObj["launch_service_provider"]?.jsonObject?.get("id")?.jsonPrimitive?.content
    val rocketID= launchJsonObj["rocket"]?.jsonObject?.get("id")?.jsonPrimitive?.content
    val rocketVersionID= launchJsonObj["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("id")?.jsonPrimitive?.content
    val launchTimeUTC = ZonedDateTime.parse(launchJsonObj["net"]?.jsonPrimitive?.content?: error("Missing 'net' field in launch JSON"))
    val padTimeZoneID = launchJsonObj["pad"]?.jsonObject?.get("location")?.jsonObject?.get("timezone_name")?.jsonPrimitive?.content?: "UTC" // fallback
    val launchTimePad: ZonedDateTime =ZonedDateTime.parse(launchJsonObj["net"]?.jsonPrimitive?.content?: error("Missing 'net' field in launch JSON")).withZoneSameInstant(ZoneId.of(padTimeZoneID))



    /*
    val toReturn=Launch(
        launchID = ,
        missionName = ,
        padName = ,
        agencyID = ,
        rocketID = ,
        rocketVersionID = ,
        launchTimeUTC = ,
        launchTimePadLocal = ,
        reusableParts = ,
        success = ,
        windowOpen = ,
        windowClose =  ,
        imageUrl =  ,
        imageCredit =  ,
        licenseType =  ,
        weatherProbability =  ,
        agencyName =  ,
        rocketName =  ,
        rocketVariantName =  ,
        description =  ,
        orbit =  ,
        padCords =  ,
        padLocation =  ,
        padCountry =  ,
        padTimeZone =
    )

     */

}
*/


/*
,
        {
            "id": "c8d6f024-0a6a-4b07-8c05-97648be4d827",
            "url": "https://lldev.thespacedevs.com/2.3.0/launches/c8d6f024-0a6a-4b07-8c05-97648be4d827/",
            "name": "Kinetica 1 | 9 satellites",
            "response_mode": "detailed",
            "slug": "kinetica-1-9-satellites",
            "launch_designator": "2025-292",
            "status": {
                "id": 3,
                "name": "Launch Successful",
                "abbrev": "Success",
                "description": "The launch vehicle successfully inserted its payload(s) into the target orbit(s)."
            },
            "last_updated": "2025-12-12T13:49:43Z",
            "net": "2025-12-10T04:03:00Z",
            "net_precision": {
                "id": 1,
                "name": "Minute",
                "abbrev": "MIN",
                "description": "The T-0 is accurate to the minute."
            },
            "window_end": "2025-12-10T04:15:00Z",
            "window_start": "2025-12-10T03:54:00Z",
            "image": {
                "id": 1755,
                "name": "[AUTO] Kinetica 1 - image",
                "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/zhongke-1a_image_20220912002218.png",
                "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305193659.png",
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
                "id": 1040,
                "url": "https://lldev.thespacedevs.com/2.3.0/agencies/1040/",
                "name": "CAS Space",
                "abbrev": "CAS",
                "type": {
                    "id": 3,
                    "name": "Commercial"
                },
                "featured": false,
                "country": [
                    {
                        "id": 6,
                        "name": "China",
                        "alpha_2_code": "CN",
                        "alpha_3_code": "CHN",
                        "nationality_name": "Chinese",
                        "nationality_name_composed": "Sino"
                    }
                ],
                "description": "CAS Space is a spin-off company from the Chinese Academy of Sciences (CAS) that is operating and plans to operate various launch vehicles of the Zhongke series.",
                "administrator": null,
                "founding_year": 2018,
                "launchers": "Zhongke",
                "spacecraft": "",
                "parent": null,
                "image": null,
                "logo": {
                    "id": 122,
                    "name": "[AUTO] CAS Space - logo",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/cas2520space_logo_20220727081352.png",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184930.png",
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
                    "id": 48,
                    "name": "[AUTO] CAS Space - social_logo",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/cas2520space_nation_20230604193435.png",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184743.png",
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
                "total_launch_count": 11,
                "consecutive_successful_launches": 5,
                "successful_launches": 10,
                "failed_launches": 1,
                "pending_launches": 1,
                "consecutive_successful_landings": 0,
                "successful_landings": 0,
                "failed_landings": 0,
                "attempted_landings": 0,
                "successful_landings_spacecraft": 0,
                "failed_landings_spacecraft": 0,
                "attempted_landings_spacecraft": 0,
                "successful_landings_payload": 0,
                "failed_landings_payload": 0,
                "attempted_landings_payload": 0,
                "info_url": "https://cas-space.com/",
                "wiki_url": "https://en.wikipedia.org/wiki/CAS_Space",
                "social_media_links": []
            },
            "rocket": {
                "id": 8835,
                "configuration": {
                    "response_mode": "detailed",
                    "id": 483,
                    "url": "https://lldev.thespacedevs.com/2.3.0/launcher_configurations/483/",
                    "name": "Kinetica 1",
                    "families": [],
                    "full_name": "Kinetica 1",
                    "variant": "",
                    "active": true,
                    "is_placeholder": false,
                    "manufacturer": {
                        "response_mode": "normal",
                        "id": 1040,
                        "url": "https://lldev.thespacedevs.com/2.3.0/agencies/1040/",
                        "name": "CAS Space",
                        "abbrev": "CAS",
                        "type": {
                            "id": 3,
                            "name": "Commercial"
                        },
                        "featured": false,
                        "country": [
                            {
                                "id": 6,
                                "name": "China",
                                "alpha_2_code": "CN",
                                "alpha_3_code": "CHN",
                                "nationality_name": "Chinese",
                                "nationality_name_composed": "Sino"
                            }
                        ],
                        "description": "CAS Space is a spin-off company from the Chinese Academy of Sciences (CAS) that is operating and plans to operate various launch vehicles of the Zhongke series.",
                        "administrator": null,
                        "founding_year": 2018,
                        "launchers": "Zhongke",
                        "spacecraft": "",
                        "parent": null,
                        "image": null,
                        "logo": {
                            "id": 122,
                            "name": "[AUTO] CAS Space - logo",
                            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/cas2520space_logo_20220727081352.png",
                            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184930.png",
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
                            "id": 48,
                            "name": "[AUTO] CAS Space - social_logo",
                            "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/cas2520space_nation_20230604193435.png",
                            "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305184743.png",
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
                        "total_launch_count": 11,
                        "consecutive_successful_launches": 5,
                        "successful_launches": 10,
                        "failed_launches": 1,
                        "pending_launches": 1,
                        "consecutive_successful_landings": 0,
                        "successful_landings": 0,
                        "failed_landings": 0,
                        "attempted_landings": 0,
                        "successful_landings_spacecraft": 0,
                        "failed_landings_spacecraft": 0,
                        "attempted_landings_spacecraft": 0,
                        "successful_landings_payload": 0,
                        "failed_landings_payload": 0,
                        "attempted_landings_payload": 0,
                        "info_url": "https://cas-space.com/",
                        "wiki_url": "https://en.wikipedia.org/wiki/CAS_Space",
                        "social_media_links": []
                    },
                    "program": [],
                    "reusable": false,
                    "image": {
                        "id": 1755,
                        "name": "[AUTO] Kinetica 1 - image",
                        "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/zhongke-1a_image_20220912002218.png",
                        "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305193659.png",
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
                    "info_url": "https://en.cas-space.com/index/index/technology",
                    "wiki_url": null,
                    "description": "Kinetica 1 (also known as Lijian-1, Chinese: 力箭一号) is a Chinese solid-propellant light launch vehicle. It is capable of placing about 2 tons into low earth orbit and 1.5 tons into sun-synchronous orbit. It is developed by CAS Space, a subsidiary of the Chinese Academy of Sciences. Kinetica 1 is a light launcher whose first stage seems to be derived from the DF-31 long-range intercontinental ballistic missile. It is 30 m high and has a diameter of 2.65 m, with a liftoff mass of 135 t. It's the largest Chinese solid propellant launcher to date, with four stages with a thrust of 200, 100, 50 and 10 tons respectively. The fairing has a diameter of 3.35 meters.",
                    "alias": "",
                    "min_stage": 4,
                    "max_stage": 4,
                    "length": 30.0,
                    "diameter": 2.65,
                    "maiden_flight": "2022-07-27",
                    "launch_cost": null,
                    "launch_mass": 135.0,
                    "leo_capacity": 2000.0,
                    "gto_capacity": null,
                    "geo_capacity": null,
                    "sso_capacity": 1500.0,
                    "to_thrust": 1961.0,
                    "apogee": null,
                    "total_launch_count": 11,
                    "consecutive_successful_launches": 5,
                    "successful_launches": 10,
                    "failed_launches": 1,
                    "pending_launches": 0,
                    "attempted_landings": 0,
                    "successful_landings": 0,
                    "failed_landings": 0,
                    "consecutive_successful_landings": 0,
                    "fastest_turnaround": "P20DT23H59M"
                },
                "launcher_stage": [],
                "spacecraft_stage": [],
                "payloads": []
            },
            "mission": {
                "id": 7426,
                "name": "9 satellites",
                "type": "Earth Science",
                "description": "Share ride of 9 satellites to sun-synchronous orbit:\r\n\r\n* Satellite 813 (United Arab Emirates)\r\n* Jilin-1 Gaofen 07B-01/07C-01/07D-01\r\n* Dongpo-15 \r\n* Yixing-2 09\r\n* Yixian-A\r\n* SPNEX (Egypt)\r\n* Slipper2Sat (Nepal)",
                "image": null,
                "orbit": {
                    "id": 17,
                    "name": "Sun-Synchronous Orbit",
                    "abbrev": "SSO",
                    "celestial_body": {
                        "response_mode": "list",
                        "id": 1,
                        "name": "Earth"
                    }
                },
                "agencies": [],
                "info_urls": [],
                "vid_urls": []
            },
            "pad": {
                "id": 151,
                "url": "https://lldev.thespacedevs.com/2.3.0/pads/151/",
                "active": true,
                "agencies": [],
                "name": "Launch Area 130",
                "image": {
                    "id": 1460,
                    "name": "Kinetica 1 liftoff from LA-130 (Flight 2)",
                    "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/kinetica25201_image_20230727064618.jpg",
                    "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305192832.jpeg",
                    "credit": "CAS Space",
                    "license": {
                        "id": 1,
                        "name": "Unknown",
                        "priority": 9,
                        "link": null
                    },
                    "single_use": false,
                    "variants": []
                },
                "description": "",
                "info_url": null,
                "wiki_url": "https://en.wikipedia.org/wiki/Jiuquan_Satellite_Launch_Center",
                "map_url": "https://www.google.com/maps?q=40.8182,100.22514",
                "latitude": 40.8182,
                "longitude": 100.22514,
                "country": {
                    "id": 6,
                    "name": "China",
                    "alpha_2_code": "CN",
                    "alpha_3_code": "CHN",
                    "nationality_name": "Chinese",
                    "nationality_name_composed": "Sino"
                },
                "map_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/map_images/pad_151_20200803143425.jpg",
                "total_launch_count": 11,
                "orbital_launch_attempt_count": 11,
                "fastest_turnaround": "P20DT23H59M",
                "location": {
                    "response_mode": "normal",
                    "id": 17,
                    "url": "https://lldev.thespacedevs.com/2.3.0/locations/17/",
                    "name": "Jiuquan Satellite Launch Center, People's Republic of China",
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
                        "id": 6,
                        "name": "China",
                        "alpha_2_code": "CN",
                        "alpha_3_code": "CHN",
                        "nationality_name": "Chinese",
                        "nationality_name_composed": "Sino"
                    },
                    "description": "Jiuquan Satellite Launch Center is a Chinese spaceport located between the Ejin, Alxa, Inner Mongolia and Hangtian Town, Jinta County, Jiuquan, Gansu Province. It is part of the Dongfeng Aerospace City (Base 10).",
                    "image": {
                        "id": 1374,
                        "name": "Long March 2F/G liftoff (Shenzhou 16)",
                        "image_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/long2520march_image_20240101085213.jpeg",
                        "thumbnail_url": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305192548.jpeg",
                        "credit": "CMS",
                        "license": {
                            "id": 1,
                            "name": "Unknown",
                            "priority": 9,
                            "link": null
                        },
                        "single_use": false,
                        "variants": []
                    },
                    "map_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/map_images/location_17_20200803142429.jpg",
                    "longitude": 100.291111,
                    "latitude": 40.958056,
                    "timezone_name": "Asia/Shanghai",
                    "total_launch_count": 272,
                    "total_landing_count": 2
                }
            },
            "webcast_live": false,
            "program": [],
            "orbital_launch_attempt_count": 7148,
            "location_launch_attempt_count": 270,
            "pad_launch_attempt_count": 11,
            "agency_launch_attempt_count": 11,
            "orbital_launch_attempt_count_year": 299,
            "location_launch_attempt_count_year": 31,
            "pad_launch_attempt_count_year": 5,
            "agency_launch_attempt_count_year": 5,
            "flightclub_url": null,
            "updates": [
                {
                    "id": 13227,
                    "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/cosmic2520penguin_profile_20210817212020.png",
                    "comment": "Launch success.",
                    "info_url": "https://x.com/cas_space/status/1998613252561912098",
                    "created_by": "Cosmic_Penguin",
                    "created_on": "2025-12-10T04:43:00Z"
                },
                {
                    "id": 13135,
                    "profile_image": "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/profile_images/cosmic2520penguin_profile_20210817212020.png",
                    "comment": "Added launch per NOTAMs; Payload identities uncertain.",
                    "info_url": "https://forum.nasaspaceflight.com/index.php?topic=57867.msg2739945#msg2739945",
                    "created_by": "Cosmic_Penguin",
                    "created_on": "2025-12-04T17:00:00Z"
                }
            ],
            "info_urls": [],
            "vid_urls": [
                {
                    "priority": 10,
                    "source": null,
                    "publisher": "SciNews",
                    "title": "Lijian-1 launches 9 satellites",
                    "description": "A Lijian-1 launch vehicle (力箭一号遥十一, Lijian-1 Y11) launched 9 satellites (Satellite 813, Jilin Gaofen 07B01/07C01/07D01, Dongpo-15, Yixing-2 09, Yixian-A, SPNEX, Slipper2Sat) from the “Dongfeng Commercial Space Innovation Experimental Zone” (东风商业航天创新试验区), Jiuquan Satellite Launch Center, China, on 10 December 2025, at 04:03 UTC (12:03 local time). The Lijian-1 rocket (力箭一号), also known as ZK-1A and Kinetica-1, is a four-stage solid-propellant launch vehicle jointly developed by the Institute of Mechanics of the Chinese Academy of Sciences and CAS Space (Beijing Zhongke Aerospace Exploration Technology Co., Ltd.). \nCredit: China Central Television (CCTV)/CAS Space",
                    "feature_image": "https://i.ytimg.com/vi/uddDgQBW7uA/maxresdefault.jpg",
                    "url": "https://www.youtube.com/watch?v=uddDgQBW7uA",
                    "type": {
                        "id": 6,
                        "name": "Recap"
                    },
                    "language": {
                        "id": 1,
                        "name": "English",
                        "code": "en"
                    },
                    "start_time": null,
                    "end_time": null,
                    "live": false
                }
            ],
            "timeline": [],
            "pad_turnaround": "P31DT31M",
            "mission_patches": []
        }
    ]
}
 */
