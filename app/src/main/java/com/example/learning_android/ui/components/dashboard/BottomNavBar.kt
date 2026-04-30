    package com.example.learning_android.ui.components.dashboard



    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.offset
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.layout.width
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.foundation.shape.GenericShape
    import androidx.compose.material3.FloatingActionButton
    import androidx.compose.material3.Icon
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.NavigationBarItem
    import androidx.compose.material3.NavigationBarItemDefaults
    import androidx.compose.material3.Surface
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.remember
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.geometry.Rect
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.platform.LocalDensity
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.example.learning_android.R
    import com.example.learning_android.domain.model.DashboardNav

    @Composable
    fun BottomNavBar(onNavClick: (nav: DashboardNav) -> Unit, nav: DashboardNav) {
        val density = LocalDensity.current


        val cutoutShape = remember(density) {
            GenericShape { size, _ ->
                val cutoutRadiusPx = with(density) { 40.dp.toPx() }
                val center = size.width / 2f

                moveTo(0f, 0f)

                lineTo(center - cutoutRadiusPx, 0f)

                arcTo(
                    rect = Rect(
                        left = center - cutoutRadiusPx,
                        top = -cutoutRadiusPx,
                        right = center + cutoutRadiusPx,
                        bottom = cutoutRadiusPx
                    ),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = -180f,
                    forceMoveTo = false
                )

                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = cutoutShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .height(70.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        NavItem(
                            label = "Devices",
                            isSelected = nav == DashboardNav.DEVICES,
                            onClick = { onNavClick(DashboardNav.DEVICES)},
                            painter = painterResource(id = R.drawable.ic_device_nav_icon)
                        )
                    }

                    Spacer(modifier = Modifier.width(80.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .height(70.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        NavItem(
                            label = "Places",
                            isSelected = nav == DashboardNav.PLACES,
                            onClick = { onNavClick(DashboardNav.PLACES)},
                            painter = painterResource(R.drawable.ic_places_nav_icon)
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = {},
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
                modifier = Modifier
                    .offset(y = (-38).dp)
                    .size(64.dp)
            ) {
                Text("+", fontSize = 32.sp, color = Color.White)
            }
        }
    }