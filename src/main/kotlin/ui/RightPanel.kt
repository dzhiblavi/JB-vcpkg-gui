package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import model.VcPackage
import model.VcpkgModel
import util.Config

@Composable
fun displayPackage(pkg: VcPackage, model: VcpkgModel) {
    ListItem(
        modifier = Modifier
            .padding(Config.DEFAULT_PADDING)
            .background(color = (
                    if (pkg in model.selectedPackages)
                        Config.COLORS.primary
                    else
                        Config.COLORS.secondary
                    ))
            .clickable {
            if (pkg in model.selectedPackages) {
                model.selectedPackages.remove(pkg)
            } else {
                model.selectedPackages.add(pkg)
            }
        }) {

        Row(
            modifier = Modifier
        ) {
            Text(pkg.name, Modifier
                .padding(Config.DEFAULT_PADDING))
            Text(pkg.version, Modifier
                .padding(Config.DEFAULT_PADDING))
            Text(pkg.desc, Modifier
                .padding(Config.DEFAULT_PADDING))
        }
    }

    Divider()
}

@Composable
fun rightPanel(model: VcpkgModel) {
    Box(
        modifier = Modifier
            .padding(Config.DEFAULT_PADDING)
            .clip(Config.CORNER_SHAPE)
            .border(Config.BORDER_STROKE)
            .background(Config.COLORS.background)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val scrollState1 = rememberLazyListState()
        val scrollState2 = rememberLazyListState()

        val columnModifier = Modifier
            .height(400.dp)
            .padding(Config.DEFAULT_PADDING)

        Column(
            modifier = Modifier.padding(Config.DEFAULT_PADDING)
        ) {
            Text(
                text = "Installed",
                modifier = Modifier.padding(Config.DEFAULT_PADDING)
            )
            LazyColumn(
                state = scrollState1,
                modifier = columnModifier
            ) {
                items(model.installedPackages) {
                    displayPackage(it, model)
                }
            }

            Divider(
                thickness = 2.dp,
                color = Config.COLORS.onSecondary
            )

            Text(
                text = "Search results",
                modifier = Modifier.padding(Config.DEFAULT_PADDING)
            )
            LazyColumn(
                state = scrollState2,
                modifier = columnModifier)
            {
                items(model.searchedPackages) {
                    displayPackage(it, model)
                }
            }
        }
    }
}
