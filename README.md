# Recoding Framework
Framework for performing data recoding on NMDbiotic API. Recodings are systematic changes to historical registrations, either for correcting systematic errors or for standardizing registrations which has been done inconcsistently or have changed over time as data model revisions have made new fields available. NMDbiotic is versioned with a resolution of 24h, so rollback is possible if quality assurance of recoding slips.

## Typical use case
A typical recoding case can be formulated as a collection of atomic updates, typically identified by searching through large sections of the database. This use case is supported via the interfaces IItemRecoder and IBatchRecoder. The latter has an abstract implementation in SimpleBatchRecoder. The repositry has an example to illustrate how extension of SimpleBatchRecoder and implementation of IItemRecoder can be used.

As the code for recoding itself serves as documentation for how recoding was done, we will clone the recoding framework for each recoding job so that we don't have to keep track of framework versioning when inspecting the recoding implemenations.

A user interface is provided for IBatchRecoder. This allows for:
* compiling recodings to be performed, and save them.
* list planned recodings
* simulate planned recodings (do all steps except updating records in database)
* preform recoding

## Quality assurance
The framework supports implementing data tests to be run before and after recoding. In addition prosedures for assuring quality of recoding and documentation are being developed.
