Feature: Register Printer Flow

     @sanity @BlacklistPayloads
     Scenario Outline: Register Printer and validate response payload
          Given a <PAYLOAD-TYPE> printer identification payload
          When register printer api with <BLACKLIST-RULE> is called
          Then the response code should be <CODE> and <ERROR-CODE>

          Examples:
               | PAYLOAD-TYPE          | CODE | BLACKLIST-RULE            | ERROR-CODE |
               | Blacklist_payload     | 403  | model_and_firmwareVersion |  AVR000017 |
               | Blacklist_payload     | 403  | model                     |  AVR000017 |
               | Blacklist_payload     | 403  | firmwareVersion           |  AVR000017 |

     @sanity @InvalidPayloads
     Scenario Outline: Register Printer with different payload
          Given a <PAYLOAD-TYPE> printer identification payload
          When register printer api is called
          Then the response code should be <CODE>
          Then the registration api should return <response_type> response <detailed_code> and <error_description>

          Examples:
               | PAYLOAD-TYPE                                      | CODE |  response_type |  detailed_code 	| 					error_description 								|
               | invalid_payload_country_mismatch                  | 409  |			error      |		AVR000101     |  Country Mismatch in Registration Payload		|
               | invalid_payload_serial_number_missing             | 409  |			error      |		AVR000102     | Invalid Serial Number in Registraion Payload|
               | invalid_payload_language_mismatch                 | 409  |			error      |		AVR000103     |  Language Mismatch in Registraion Payload		|
               | invalid_payload_entity_domain_index_missing       | 409  |			error      |		AVR000104     | Invalid Domain Index in Registraion Payload	|



     @sanity @validPayloads
     Scenario Outline: Register Printer and validate response payload
          Given a <PAYLOAD-TYPE> printer identification payload
          When register printer api is called
          Then the response code should be <CODE>
          And the response payload should be as per entity config response schema

          Examples:
               | PAYLOAD-TYPE                     | CODE |
               | valid_payload                    | 201  |

     @sanity @DLSFailing
     Scenario Outline: Register Printer with valid payload but DLS Failed
          Given a <PAYLOAD-TYPE> printer identification payload but DLS returning <DLS-RESPONSE-CODE>
          When register printer api is called
          Then the response code should be <CODE>

          Examples:
              | PAYLOAD-TYPE  | DLS-RESPONSE-CODE | CODE |
              | valid_payload | 500               | 201  |
              | valid_payload | 503               | 201  |
              | valid_payload | 404               | 201  |

