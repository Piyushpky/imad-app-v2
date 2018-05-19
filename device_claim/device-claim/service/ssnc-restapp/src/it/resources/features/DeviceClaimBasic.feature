@Morpheus
Feature: Device Lookup Service


  @sanity @DeviceLookup
  Scenario Outline: Use the device lookup service api to get information for unregistered printers using printerCode

    Given I have a valid ssn "<printerCode>"
    When I call the get printerCode info api
    Then the device lookup response code should be 404 with header as "<header>"
    When I call the delete printerCode info api
    Then the delete device lookup response code should be 404
    And cleanup the printer with "<snkey>"
    Examples:
      | printerCode      | snkey                                       | registeredflag | cloudId | inkflag |header    |
      | GQ05GVJBEL05JDVV | 4oxdnyiK3gAxNIxFNGs2_ML1MfbnQLR1qyVewDfSqVI | false          |         | true    |AVDLS000102|
      | GQW5FLH8DSLH4CH0 | ufMimQZdAKqEdGQ2OEsyVWGwLCweyvHGhM8WI3Udum8 | false          |         | false   |AVDLS000102|
      | GQ05FWJWQLAIJSCB | bwpOZCZ_mtKR-SAUw2G0hNmDsA4Uy6o0mdjP34fxIWc | false          |         | true    |AVDLS000102|
      | GQW5FFDT1WHPAFEK | YuAgtDw-lV--p6BIeMDLufrq4tVTe8uVyZqfmeLcp0E | false          |         | false   |AVDLS000102|
      | gQW5FFDT1WHPAFEK | YuAgtDw-lV--p6BIeMDLufrq4tVTe8uVyZqfmeLcp0E | false          |         | false   |AVDLS000102|
      | gQW5FFDT1 WHpAFEK | YuAgtDw-lV--p6BIeMDLufrq4tVTe8uVyZqfmeLcp0E | false          |         | false   |AVDLS000102|
  @sanity @DeviceLookup
  Scenario Outline: Use the device lookup service api to get information for registered printers using printerCode

    Given The printer with details "<snkey>", "<printerId>" and "<inkflag>" is registered
    Given I have a valid ssn "<printerCode>"
    When I call the get printerCode info api
    Then the device lookup response code should be 200
    When I call the delete printerCode info api
    Then the delete device lookup response code should be 200
    When I call the get printerCode info api
    Then the device lookup response code should be 404
      Examples:
      | printerCode      | snkey                                       | registeredflag | printerId                | inkflag |
      | GQ05GVJBEL05JDVV | 4oxdnyiK3gAxNIxFNGs2_ML1MfbnQLR1qyVewDfSqVI | true           | AQAAAAFVgPjN9QAAAAGELQk8 | true    |
      | GQW5FLH8DSLH4CH0 | ufMimQZdAKqEdGQ2OEsyVWGwLCweyvHGhM8WI3Udum8 | true           | AQAAAAFVgOqIHwAAAAGs6ICv | false   |

  @sanity
  Scenario Outline:use device lookup service to get information with wrong printerCode
    Given I have an invalid printerCode "<printerCode>"
    When I call the get printerCode info api
    Then the device lookup response code should be 400 with header as "<header>"
    When I call the delete printerCode info api
    Then the delete device lookup response code should be 400
    And cleanup the printer with "<printerCode>"
    Examples:
      | printerCode |header     |
      | printer123  |AVDLS000002|
      | heloo1234   |AVDLS000002|

  @sanity
  Scenario Outline:use device lookup service to get information with mal-formed printerCode
    Given I have an invalid printerCode "<printerCode>"
    When I call the get printerCode info api
    Then the device lookup response code should be 404 with header as "<header>"
    When I call the delete printerCode info api
    Then the delete device lookup response code should be 404
    And cleanup the printer with "<printerCode>"
    Examples:
      | printerCode      |header     |
      | GQ05GVJBEL05JDVB |AVDLS000003|
      | GQW5FLH8DSLH4CHA |AVDLS000003|


  @sanity @DeviceLookup @test
  Scenario Outline: Use the device lookup service api to validate the SSN/PrinterCode for unregistered printers

    Given I have a valid ssn "<ssn>"
    When I call the device claim api
    Then the device lookup response code should be 200
    And the payload should have "<snkey>", "<registeredflag>", "<printerId>" and "<inkflag>"
    And cleanup the printer with "<snkey>"
    Examples:
      | ssn              | snkey                                       | registeredflag | printerId | inkflag |
      | GQ05GVJBEL05JDVV | 4oxdnyiK3gAxNIxFNGs2_ML1MfbnQLR1qyVewDfSqVI | false          |           | true    |
      | GQW5FLH8DSLH4CH0 | ufMimQZdAKqEdGQ2OEsyVWGwLCweyvHGhM8WI3Udum8 | false          |           | false   |
      | GQ05FWJWQLAIJSCB | bwpOZCZ_mtKR-SAUw2G0hNmDsA4Uy6o0mdjP34fxIWc | false          |           | true    |
      | GQW5FFDT1WHPAFEK | YuAgtDw-lV--p6BIeMDLufrq4tVTe8uVyZqfmeLcp0E | false          |           | false   |

  @sanity @DeviceLookup
  Scenario Outline: Use the device lookup service api to validate the SSN/PrinterCode for domainkey Invalid printers

    Given I have a valid ssn "<ssn>"
    When I call the device claim api
    Then the device lookup response code should be 404
    # Expected Status code should be 404
    And cleanup the printer with "<snkey>"
    Examples:
      | ssn              | snkey                                       | registeredflag | printerId | inkflag |
      | AQW5FLH8DSLH4CH0 | ufMimQZdAKqEdGQ2OEsyVWGwLCweyvHGhM8WI3Udum8 | false          |           | true    |


  @sanity @DeviceLookup
  Scenario Outline: Use the device lookup service api to validate the SSN/PrinterCode for registered printers

    Given I have a valid ssn "<ssn>"
    And The printer with details "<snkey>", "<printerId>" and "<inkflag>" is registered
    When I call the device claim api
    Then the device lookup response code should be 200
    And the payload should have "<snkey>", "<registeredflag>", "<printerId>" and "<inkflag>"
    And cleanup the printer with "<snkey>"
    Examples:
      | ssn              | snkey                                       | registeredflag | printerId                | inkflag |
      | GQ05GVJBEL05JDVV | 4oxdnyiK3gAxNIxFNGs2_ML1MfbnQLR1qyVewDfSqVI | true           | AQAAAAFVgPjN9QAAAAGELQk8 | true    |
      | GQW5FLH8DSLH4CH0 | ufMimQZdAKqEdGQ2OEsyVWGwLCweyvHGhM8WI3Udum8 | true           | AQAAAAFVgOqIHwAAAAGs6ICv | false   |
      | GQ05FWJWQLAIJSCB | bwpOZCZ_mtKR-SAUw2G0hNmDsA4Uy6o0mdjP34fxIWc | true           | AQAAAAFVgPjN9QAAAAGELQk8 | true    |
      | GQW5FFDT1WHPAFEK | YuAgtDw-lV--p6BIeMDLufrq4tVTe8uVyZqfmeLcp0E | true           | AQAAAAFVgOqIHwAAAAGs6ICv | false   |

  @sanity @DeviceLookup
  Scenario Outline: Use the device lookup service api to get Info by SNKey for registered printers

    Given The printer with details "<snkey>", "<printerId>" and "<inkflag>" is registered
    When I call the get info by snkey api for "<snkey>"
    Then the device lookup response code should be 200
    And the snkey info response payload should have "<snkey>", "<registeredflag>", "<printerId>" and "<inkflag>"
    And cleanup the printer with "<snkey>"
    Examples:
      | snkey                                       | registeredflag | printerId                | inkflag |
      | 4oxdnyiK3gAxNIxFNGs2_ML1MfbnQLR1qyVewDfSqVI | true           | AQAAAAFVgPjN9QAAAAGELQk8 | true    |
      | ufMimQZdAKqEdGQ2OEsyVWGwLCweyvHGhM8WI3Udum8 | true           | AQAAAAFVgOqIHwAAAAGs6ICv | false   |

  @sanity @DeviceLookup
  Scenario Outline: Use the printer registration notification api to save cloudId

    Given The registration of printer with cloud Id "<cloudId>" and serial number "<serialNum>" is notified to device lookup
    When I call the printer registration notification api
    Then the device lookup response code should be 200
    And the "<cloudId>" should be updated in the database with the correct "<snkey>"
    And cleanup the printer with "<snkey>"
    Examples:
      | cloudId                  | serialNum  | snkey                                       |
      | AQAAAAFVgPjN9QAAAAGELQk8 | JWQLAIJSCB | bwpOZCZ_mtKR-SAUw2G0hNmDsA4Uy6o0mdjP34fxIWc |
      | AQAAAAFVgOqIHwAAAAGs6ICv | DT1WHPAFEK | YuAgtDw-lV--p6BIeMDLufrq4tVTe8uVyZqfmeLcp0E |

  @sanity @DeviceLookup
  Scenario Outline: Use the device lookup service api to get Info by SNKey for registered printers with Invalid SNKey

    When I call the get info by snkey api for "<snkey>"
    Then the device lookup response code should be 404 with header as "<header>"
    And the snkey info response payload should have "<snkey>", "<registeredflag>", "<printerId>" and "<inkflag>"
    And cleanup the printer with "<snkey>"
    Examples:
      | snkey                                     | registeredflag | printerId                | inkflag | header    |
      | 4oxdnyiK3gAxNIxFNGs2_ML1MfbnQLR1qyVewDfSq | true           | AQAAAAFVgPjN9QAAAAGELQk8 | true    |AVDLS000102|
      | ufMimQZdAKqEdGQ2OEsyVWGwLCweyvHGhM8WI3Udu | true           | AQAAAAFVgOqIHwAAAAGs6ICv | false   |AVDLS000102|

  @sanity @DeviceLookup
  Scenario Outline: Use the device lookup service api to get Info by SNKey for registered printers with different domain key and serial number

    When I call the get info by snkey api for "<snkey>"
    Then the device lookup response code should be 404 with header as "<header>"
    And the snkey info response payload should have "<snkey>", "<registeredflag>", "<printerId>" and "<inkflag>"
    And cleanup the printer with "<snkey>"
    Examples:
      | snkey                                       | registeredflag | printerId                | inkflag | header    |
      | 4oxdnyiK3gAxNIxFNGs2_ML1MfbnQLR1qyVewDfABCD | true           | AQAAAAFVgPjN9QAAAAGELQk8 | true    |AVDLS000102|
      | ufMimQZdAKqEdGQ2OEsyVWGwLCweyvHGhM8WI3UABCD | true           | AQAAAAFVgOqIHwAAAAGs6ICv | false   |AVDLS000102|

  @sanity @DeviceLookup
  Scenario Outline: Use the printer registration notification api to save cloudId and claim the device

    Given The registration of printer with cloud Id "<cloudId>" and serial number "<serialNum>" is notified to device lookup
    When I call the printer registration notification api
    Then the device lookup response code should be 200
    And the "<cloudId>" should be updated in the database with the correct "<snkey>"
    When I call the post reg notification by printerId api for "<cloudId>"
    Then the reg based response code should be 200
    Given I have a valid ssn "<ssn>"
    When I call the device claim api
    Then the device lookup response code should be 200
    And the payload should have "<snkey>", "<registeredflag>", "<printerId>" and "<inkflag>"
    When I call the GET Info by printerId and claimCode api for printer "<cloudId>" and claimcode "<snkey>"
    Then the reg based response code should be 200
    And cleanup the printer with "<snkey>"
    Examples:
      | ssn              | cloudId                  | serialNum  | snkey                                       | registeredflag | printerId | inkflag |
      | GQW5FLH8DSLH4CH0 | AQAAAAFVgPjN9QAAAAGELQk8 | H8DSLH4CH0 | ufMimQZdAKqEdGQ2OEsyVWGwLCweyvHGhM8WI3Udum8 | false           |           | false   |

  @sanity @DeviceLookup
  Scenario Outline: Use the printer registration notification api to save cloudId after claim the device

    Given I have a valid ssn "<ssn>"
    When I call the device claim api
    Then the device lookup response code should be 200
    And the payload should have "<snkey>", "<registeredflag>", "<printerId>" and "<inkflag>"
    Given The registration of printer with cloud Id "<cloudId>" and serial number "<serialNum>" is notified to device lookup
    When I call the printer registration notification api
    Then the device lookup response code should be 200
    And the "<cloudId>" should be updated in the database with the correct "<snkey>"
    And cleanup the printer with "<snkey>"
    Examples:
      | ssn              | cloudId                  | serialNum  | snkey                                       | registeredflag | printerId | inkflag |
      | GQW5FLH8DSLH4CH0 | AQAAAAFVgPjN9QAAAAGELQk8 | H8DSLH4CH0 | ufMimQZdAKqEdGQ2OEsyVWGwLCweyvHGhM8WI3Udum8 | false          |           | false   |

  @sanity @DeviceLookup
  Scenario Outline: Printer registration notification api negative test cases

    Given The registration of printer with cloud Id "<cloudId>" and serial number "<serialNum>" is notified to device lookup
    When I call the printer registration notification api
    Then the device lookup response code should be 200
    # Expected Status code should be 400
    And cleanup the printer with "<snkey>"
    Examples:
      | cloudId                  | serialNum  | snkey                                       |
      | AQAAAAFVgPjN9QAAAAGELQk8 |            | bwpOZCZ_mtKR-SAUw2G0hNmDsA4Uy6o0mdjP34fxIWc |
      |                          | JWQLAIJSCB | bwpOZCZ_mtKR-SAUw2G0hNmDsA4Uy6o0mdjP34fxIWc |
      |                          |            | bwpOZCZ_mtKR-SAUw2G0hNmDsA4Uy6o0mdjP34fxIWc |