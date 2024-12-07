use coursework
db.createCollection("grammars", {
   validator: {
      $jsonSchema: {
         bsonType: "object",
         title: "CFG Object Validation",
         additionalProperties: false,
         required: ["nonTerminals", "terminals", "definingEquations", "startSymbol" ],
         properties: {
            _id: { bsonType: "objectId" },
            nonTerminals: {
               bsonType: "array",
               minItems: 1,
               items: {
                   bsonType: "string"
               },
               description: "'nonTerminals' must be an array of strings"
            },
            terminals: {
               bsonType: "array",
               minItems: 1,
               items: {
                   bsonType: "string"
               },
               description: "'terminals' must be an array of strings"
            },
            definingEquations: {
               bsonType: "array",
               minItems: 1,
               items: {
                   bsonType: "string"
               },
               description: "'definingEquations' must be an array of strings"
            },
            startSymbol: {
               bsonType: "string",
               description: "'startSymbol' should be a string"
            }
         }
      }
   }
});