INSERT INTO cfg(content, time_created)
    VALUES
    (FORMAT('{
        "startSymbol" : "R",
        "nonTerminals": [
            "E", "T", "R","S", "L"
        ],
        "terminals": [
            "+", "-","a", "i", "(", ")", "#", "_"
        ],
        "definingEquations": [
            "L = LSSLT | LTR | S+",
            "S = +S-",
            "E = TR",
            "R = +TR|-TR| _",
            "T = a|i|(E)|-TR|+T#T"
        ]
    }')::jsonb,
    NOW()),
    (FORMAT('{
        "startSymbol" : "R",
        "nonTerminals": [
            "E", "T", "R","S", "L"
        ],
        "terminals": [
            "+", "-","a", "i", "(", ")", "#", "_"
        ],
        "definingEquations": [
            "L = L",
            "S = +S-",
            "E = TRT",
            "R = +_",
            "T = a"
        ]
    }')::jsonb,
    NOW()),
    (FORMAT('{
        "startSymbol" : "R",
        "nonTerminals": [
            "E", "T", "R","S", "L"
        ],
        "terminals": [
            "+", "-","a", "i", "(", ")", "#", "_"
        ],
        "definingEquations": [
            "L = LSSLT | LTR | S+",
            "S = +S-",
            "E = TR",
            "R = +TR|-TR| _",
            "T = a|+T#T"
        ]
    }')::jsonb,
    NOW()),
    (FORMAT('{
        "startSymbol" : "R",
        "nonTerminals": [
            "E", "T", "R","S", "L"
        ],
        "terminals": [
            "+", "-","a", "i", "(", ")", "#", "_"
        ],
        "definingEquations": [
            "L = LSSLT | LTR | S+",
            "S = +S-",
            "E = TR",
            "R = +TR | _",
            "T = a|i|(E)|-TR|+T#T"
        ]
    }')::jsonb,
    NOW());
