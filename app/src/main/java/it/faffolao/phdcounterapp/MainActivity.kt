package it.faffolao.phdcounterapp

import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.faffolao.phdcounterapp.counter.GradientNumber
import it.faffolao.phdcounterapp.navmenu.NavigationItem
import it.faffolao.phdcounterapp.toolbars.Toolbar
import it.faffolao.phdcounterapp.ui.theme.PHDCounterTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PHDCounterTheme {
                // elementi del menu hamburger
                val navMenuItems = listOf(
                    NavigationItem(
                        title = "Add credit",
                        selectedIcon = Icons.Outlined.Add,
                        unselectedIcon = Icons.Outlined.Add
                    ),
                    NavigationItem(
                        title = "Remove credit",
                        selectedIcon = Icons.Outlined.Remove,
                        unselectedIcon = Icons.Outlined.Remove
                    ),
                    NavigationItem(
                        title = "Reset credit count",
                        selectedIcon = Icons.Outlined.Refresh,
                        unselectedIcon = Icons.Outlined.Refresh
                    ),
                    NavigationItem(
                        title = "About this app",
                        selectedIcon = Icons.Outlined.Info,
                        unselectedIcon = Icons.Outlined.Info
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
                                            test(item.title)
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
                                GradientNumber(numberText = 0)
                            }
                            Box(
                                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)
                            ) {
                                Toolbar(modifier = Modifier.align(Alignment.BottomCenter))
                            }
                        }
                    }
                }
            }
        }
    }

    fun test(text: String) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(this, text, duration)
        toast.show()
    }
}
