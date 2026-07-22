package com.example.myprofile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.myprofile.screens.LoginScreen
import com.example.myprofile.screens.BerandaScreen
import com.example.myprofile.screens.ProfileScreen
import com.example.myprofile.screens.DaftarNilaiScreen
import com.example.myprofile.ui.theme.MyProfileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }

            MyProfileTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(onLoginSuccess = { nim ->
                                navController.navigate("beranda/$nim") {
                                    popUpTo("login") { inclusive = true }
                                }
                            })
                        }
                        composable(
                            route = "beranda/{nim}",
                            arguments = listOf(navArgument("nim") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val nim = backStackEntry.arguments?.getString("nim") ?: ""
                            BerandaScreen(
                                nim = nim,
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo("beranda/$nim") { inclusive = true }
                                    }
                                },
                                onNavigateToProfile = {
                                    navController.navigate("profile/$nim")
                                },
                                onNavigateToNilai = {
                                    navController.navigate("daftar_nilai")
                                }
                            )
                        }
                        composable(
                            route = "profile/{nim}",
                            arguments = listOf(navArgument("nim") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val nim = backStackEntry.arguments?.getString("nim") ?: ""
                            ProfileScreen(
                                nim = nim,
                                onBackPressed = {
                                    navController.popBackStack()
                                },
                                isDarkMode = isDarkMode,
                                onDarkModeToggle = { isDarkMode = it }
                            )
                        }
                        composable("daftar_nilai") {
                            DaftarNilaiScreen(
                                onBackPressed = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
