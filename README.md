# STANDARD OPERATING PROCEDURE (SOP)

**Document Owner:** Shreyas S Rai  

---

## 1. PROJECT OVERVIEW & STRATEGIC VISION

### 1.1 Project Identity
- **Platform:** Android (Minimum SDK 24, Target SDK 34)

### 1.2 Target Audience Profile
- Digital literacy: Low to Moderate

**Secondary Users:**
- Agricultural extension workers
- Village-level dairy cooperatives
- Veterinary field officers

### 1.3 Core Problem Statement
1. **Information Gap:** Lack of scientific knowledge about balanced cattle nutrition
2. **Economic Pressure:** High cost of commercial feed compounds (₹25-35/kg)
3. **Resource Misallocation:** Suboptimal milk yield due to improper feeding practices

### 1.4 Solution Design Philosophy

  - Minimum touch target: 48x48 dp (WCAG AAA compliance)
  - Color contrast: Minimum 4.5:1 for all critical UI elements

**Offline-First Architecture:**
- 100% core functionality without internet
- Maximum cold-start time: 2 seconds
- Asset optimization: All images < 100KB (WebP format)

**Localization Strategy:**
- Phase 2: Marathi, Punjabi, Tamil, Telugu, Gujarati
- Numeral system: International (0-9) with optional Devanagari overlay

---

## 2. TECHNICAL ARCHITECTURE

### 2.1 Technology Stack

**Frontend Layer:**
```
- Navigation: Compose Navigation 2.7.x
- State Management: ViewModel + StateFlow
```

**Data Layer:**
```
- Local Database: Room 2.6.x (SQLite wrapper)
- Data Serialization: Kotlinx Serialization 1.6.x
```

**Logic Layer:**
```
- Language: Kotlin 1.9.x
- Coroutines: Kotlinx Coroutines 1.7.x
- Date/Time: Kotlinx DateTime
- Math Library: Custom nutrition calculation engine
```

**Analytics & Visualization:**
```
- Crash Reporting: Firebase Crashlytics (optional, offline-compatible)
```

**AI/ML Layer:**
```
- Primary: Gemini Nano (on-device, Android 14+)
- Fallback: Gemini API 1.5 Flash (requires internet)
- Prompt Engineering: Custom templates stored in Room DB
- Response Caching: 7-day cache for repeated queries
```

### 2.2 Architecture Pattern

**Clean Architecture Implementation:**
```
app/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   ├── entities/
│   │   └── database/
│   ├── repository/
│   └── models/
├── domain/
│   ├── usecases/
│   ├── models/
│   └── repository/
├── presentation/
│   ├── screens/
│   │   ├── onboarding/
│   │   ├── profile/
│   │   ├── recipe/
│   │   ├── comparison/
│   │   └── genai/
│   ├── components/
│   ├── theme/
│   └── navigation/
```

### 2.3 Offline-First Strategy

**Data Synchronization Rules:**
```
- Saved recipes

- GenAI conversation history
- Usage statistics (anonymized)

- Full app data export (encrypted)
- Cross-device profile transfer
```

**Offline GenAI Handling:**
```kotlin
    return when {
        isGeminiNanoAvailable() -> {
            }
        isNetworkAvailable() -> {
            geminiApi.generateExplanation(ingredientId).also {
                cacheResponse(ingredientId, it, expiryDays = 7)
            }
        }
        else -> {
        }
    }
}
```

---




**AI Builder Prompt:**
```






ACCESSIBILITY:
```

```kotlin

}
```




```


---



**AI Builder Prompt:**
```








```

```kotlin
}






  
    
    
    


```


```kotlin

}

    }
    
    } else {
    }
    }
}


```


```kotlin

  



  


    }
    }
    
    }
    
}
```





**AI Builder Prompt:**
```



  



EMPTY STATE:

ERROR STATE:
```

```kotlin
@Composable
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
    ) { padding ->
                Column {
                    )
                }
            }
        }
    }
}

@Composable
fun IngredientCard(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClick),
    ) {
                modifier = Modifier
                    .fillMaxWidth()
            )
            
                Text(
                )
                
                Text(
                )
                
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                
                )
            }
        }
    }
}
```


```kotlin
)

}

)

}

```





**AI Builder Prompt:**
```
  


  
  


  

```kotlin
@Composable
) {
    
    }
    
    Column(
    ) {
        Box(
            modifier = Modifier
        ) {
            ) {
                )
            
            ) {
                )
            
                )
            
        
        
    }
}
}
}
}

    )
}

@Composable
fun SavingsChart(
    timePeriod: TimePeriod
) {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                
                
                axisRight.isEnabled = false
                legend.isEnabled = false
                
                }
                    lineWidth = 3f
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                    
                }
                
            chart.invalidate()
        }
    )
}

)

}
```

---


```kotlin
@Composable
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
                onComplete = {
                    }
                }
            )
        }
        
                    }
                }
            )
        }
        
        composable(
                },
                }
            )
        }
        
    }
}

```

---



```kotlin
): Float {
    }
    
): Float {
    }
    
): Float {
    }
    }
}
```


```kotlin
)

    
    
    
    
    
    val concentrateMix = optimizeConcentrateMix(
    )
    
    
    return FeedRecipe(
    )
}

    
        }
    
        if (remainingDMI <= 0) break
        
            remainingDMI,
        )
        
        
        }
    }
    
}

    
    } else {
    }
    
    }
    

---



```kotlin
@Database(
    entities = [
        FeedRecipeEntity::class,
        RecipeIngredientCrossRef::class,
    ],
    exportSchema = true
)
abstract class PashuAaharDatabase : RoomDatabase() {
    abstract fun cowProfileDao(): CowProfileDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun genAICacheDao(): GenAICacheDao
}
```

```kotlin
@Entity(tableName = "cow_profiles")
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val ageMonths: Int,
    val weightKg: Int,
    
    val isPregnant: Boolean = false,
    
    val createdAt: Long = System.currentTimeMillis(),
)
```

```kotlin
    val nameEnglish: String,
    val nameHindi: String,
    
    val dryMatterPercent: Float,
    val calciumPercent: Float,
    val phosphorusPercent: Float,
    
    
    
    

            )
        
    }
}
```


```kotlin
)
```


```kotlin
    
    
    
    
    
    

```kotlin
    
    
    
    
    
    
}

        }
        
    }
    
    ): String {
    }
    
    ): String {
        return """
            
            
            
        """.trimIndent()
    }
}
```


```kotlin
@Composable
) {
            .fillMaxWidth()
    ) {
                contentDescription = null,
        )

        
        Text(
        )
        Text(
        )
        
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    )
                }
            }
        )
    }
}

@Composable
) {
    Column(
        ) {
            Icon(
                imageVector = icon,
            )
        
        
        Text(
            text = label,
        )
    }
}




```kotlin
class NutritionCalculatorTest {
    
    @Test
            weightKg = 400,
        )
        
    }
    
    @Test
        
    }
    
    @Test
        )
        
    }
    
    @Test
        
    }
    
    @Test
        
        )
        
    }
}
```


```kotlin
@RunWith(AndroidJUnit4::class)
class CowProfileDaoTest {
    
    private lateinit var database: PashuAaharDatabase
    private lateinit var cowProfileDao: CowProfileDao
    
    @Before
        cowProfileDao = database.cowProfileDao()
    }
    
    @After
        database.close()
    }
    
    @Test
            weightKg = 400,
        )
        
        
    }
    
    @Test
            weightKg = 400,
        )
        
        
    }
    
    @Test
            ageMonths = 48,
        )
        
        
        
    }
}
```


```kotlin
@RunWith(AndroidJUnit4::class)
    
    @get:Rule
    
    @Test
            }
        
        
        
    }
    
    @Test
        composeTestRule.setContent {
            
                            .assertIsDisplayed()
                    }
                            .assertIsDisplayed()
                    }
        }
        
        
    }
}
```


```kotlin
@Test
    
}

@Test
    
    
    
}
```

---



```gradle
android {
    
    defaultConfig {
        
        
            }
        }
    }
    
    buildTypes {
        debug {
        }
    }
    
    buildFeatures {
    }
    
    composeOptions {
    }
    
    packagingOptions {
        resources {
        }
    }
}
```


# Keep Room entities

-keep class dagger.hilt.** { *; }



}
```





```kotlin

        }
    }
    
        

```kotlin
}
```

---





---





---

