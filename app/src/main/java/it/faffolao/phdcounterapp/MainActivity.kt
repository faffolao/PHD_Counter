package it.faffolao.phdcounterapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import it.faffolao.phdcounterapp.counter.GradientNumber
import it.faffolao.phdcounterapp.counter.ResetCountDialog
import it.faffolao.phdcounterapp.navmenu.NavigationItem
import it.faffolao.phdcounterapp.toolbars.Toolbar
import it.faffolao.phdcounterapp.ui.theme.PHDCounterTheme
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// inizializzazione datastore
private const val USER_PREFERENCES_NAME = "credits"
private val Context.dataStore by preferencesDataStore(name = USER_PREFERENCES_NAME)
val CREDIT_COUNT_KEY = intPreferencesKey("credit_count")

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PHDCounterTheme {
                // ottenimento del valore dei crediti dal DataStore
                val countFlow = remember {
                    dataStore.data.map { preferences ->
                        preferences[CREDIT_COUNT_KEY] ?: 0
                    }
                }
                val count by countFlow.collectAsState(initial = 0)
                val scope = rememberCoroutineScope()

                // definizione funzione per l'aggiornamento della conta dei crediti sul DataStore
                val updateCount = { newValue: Int ->
                    scope.launch {
                        dataStore.edit { preferences ->
                            preferences[CREDIT_COUNT_KEY] = newValue
                        }
                    }
                }

                // indica se le finestre di dialogo per il reset del contatore e le info sull'app
                // sono aperte
                var showResetDialog by remember { mutableStateOf(false) }
                var showAboutUsDialog by remember { mutableStateOf(false) }

                // elementi del menu hamburger
                val navMenuItems = listOf(
                    NavigationItem(
                        title = "Add credit",
                        selectedIcon = Icons.Outlined.Add,
                        unselectedIcon = Icons.Outlined.Add,
                        fn = { updateCount(count + 1) }
                    ),
                    NavigationItem(
                        title = "Remove credit",
                        selectedIcon = Icons.Outlined.Remove,
                        unselectedIcon = Icons.Outlined.Remove,
                        fn = { if (count > 0) updateCount(count - 1) }
                    ),
                    NavigationItem(
                        title = "Reset credit count",
                        selectedIcon = Icons.Outlined.Refresh,
                        unselectedIcon = Icons.Outlined.Refresh,
                        fn = { showResetDialog = true }
                    ),
                    NavigationItem(
                        title = "About this app",
                        selectedIcon = Icons.Outlined.Info,
                        unselectedIcon = Icons.Outlined.Info,
                        fn = { showAboutUsDialog = true }
                    )
                )

                // contenuto dell'activity (Surface)
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()

                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet {
                                navMenuItems.forEachIndexed { index, item ->
                                    NavigationDrawerItem(
                                        label = { Text(text = item.title) },
                                        selected = false,
                                        onClick = {
                                            scope.launch {
                                                drawerState.close()
                                            }
                                            item.fn(count)
                                        },
                                        icon = {
                                            Icon(imageVector = item.selectedIcon, contentDescription = item.title)
                                        },
                                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                    )
                                }
                            }
                        }
                    ) {
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            topBar = {
                                CenterAlignedTopAppBar(
                                    title = {
                                        Text(text = "Hairdryer Count")
                                    },
                                    navigationIcon = {
                                        IconButton(onClick = {
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        }) {
                                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                                        }
                                    }
                                )
                            }
                        ) { innerPadding ->
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                GradientNumber(
                                    count = count,
                                    onCounterClick = { if (count > 0) updateCount(count - 1) }
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                                    .padding(16.dp)
                            ) {
                                Toolbar(
                                    modifier = Modifier.align(Alignment.BottomCenter),
                                    onAddClick = { updateCount(count + 1) },
                                    onRemoveClick = { if (count > 0) updateCount(count - 1) },
                                    onResetClick = { showResetDialog = true }
                                )

                                // visualizzo i dialogs quando vengono richiesti
                                if (showResetDialog) {
                                    ResetCountDialog(
                                        onDismissRequest = { showResetDialog = false },
                                        onConfirmation = { newValue ->
                                            updateCount(newValue)
                                            showResetDialog = false
                                        }
                                    )
                                }
                                if (showAboutUsDialog) {
                                    AboutUsDialog (
                                        onDismissRequest = { showAboutUsDialog = false }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
