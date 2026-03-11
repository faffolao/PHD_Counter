package it.faffolao.phdcounterapp.toolbars

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import it.faffolao.phdcounterapp.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Toolbar(modifier: Modifier = Modifier) {
    HorizontalFloatingToolbar(
        modifier = modifier,
        expanded = true,
        // colore della toolbar
        colors = FloatingToolbarDefaults.vibrantFloatingToolbarColors(),
        // ombreggiatura della toolbar
        expandedShadowElevation = 3.dp
    ) {
        listOf(
            R.drawable.outline_add_24,
            R.drawable.outline_autorenew_24,
            R.drawable.outline_check_indeterminate_small_24
        ).forEach { iconRes ->
            IconButton(
                onClick = { /*todo*/ }
            ) {
                Icon(imageVector = ImageVector.vectorResource(iconRes), contentDescription = "Add 1 credit")
            }
        }
    }
}
