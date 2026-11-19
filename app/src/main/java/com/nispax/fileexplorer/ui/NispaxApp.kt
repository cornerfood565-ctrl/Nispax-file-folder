package com.nispax.fileexplorer.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import com.nispax.fileexplorer.ui.theme.*
import java.text.DecimalFormat

private enum class Tab(val label: String, val icon: ImageVector) {
    Home("Home", Icons.Default.Home),
    Browse("Browse", Icons.Default.Folder),
    Storage("Storage", Icons.Default.DonutLarge),
    Tools("Tools", Icons.Default.Build),
    Settings("Settings", Icons.Default.Settings)
}

@Composable
fun NispaxApp() {
    var selectedTab by remember { mutableStateOf(Tab.Home) }
    var selectedTree by remember { mutableStateOf<Uri?>(null) }
    var currentFolder by remember { mutableStateOf<DocumentFile?>(null) }
    val context = LocalContext.current
    val folderPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
        if (uri != null) {
            runCatching {
                context.contentResolver.takePersistableUriPermission(
                    uri, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
            selectedTree = uri
            currentFolder = DocumentFile.fromTreeUri(context, uri)
            selectedTab = Tab.Browse
        }
    }

    Scaffold(
        containerColor = NispaxBlack,
        bottomBar = {
            NavigationBar(containerColor = NispaxSurface, contentColor = NispaxText) {
                Tab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NispaxBlue,
                            selectedTextColor = NispaxBlue,
                            indicatorColor = NispaxBlue.copy(alpha = 0.14f),
                            unselectedIconColor = NispaxMuted,
                            unselectedTextColor = NispaxMuted
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            if (selectedTab == Tab.Browse) {
                FloatingActionButton(onClick = { folderPicker.launch(null) }, containerColor = NispaxBlue) {
                    Icon(Icons.Default.Add, contentDescription = "Choose folder")
                }
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).background(NispaxBlack)) {
            when (selectedTab) {
                Tab.Home -> HomeScreen(onBrowse = { selectedTab = Tab.Browse }, onChoose = { folderPicker.launch(null) })
                Tab.Browse -> BrowseScreen(folder = currentFolder, onChoose = { folderPicker.launch(null) }, onOpen = { currentFolder = it })
                Tab.Storage -> StorageScreen(onChoose = { folderPicker.launch(null) })
                Tab.Tools -> ToolsScreen()
                Tab.Settings -> SettingsScreen()
            }
        }
    }
}

@Composable
private fun ScreenHeader(title: String, subtitle: String) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 18.dp)) {
        Text("NISPAX", style = MaterialTheme.typography.labelLarge, color = NispaxBlue, fontWeight = FontWeight.Bold)
        Text(title, style = MaterialTheme.typography.headlineMedium, color = NispaxText, fontWeight = FontWeight.Bold)
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = NispaxMuted)
    }
}

@Composable
private fun HomeScreen(onBrowse: () -> Unit, onChoose: () -> Unit) {
    ScreenHeader("File Explorer", "Your files, organized.")
    LazyColumn(contentPadding = PaddingValues(horizontal = 18.dp, vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = NispaxSurface), shape = RoundedCornerShape(22.dp)) {
                Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Storage, null, tint = NispaxBlue)
                        Spacer(Modifier.width(8.dp))
                        Text("STORAGE ACCESS", color = NispaxMuted, style = MaterialTheme.typography.labelMedium)
                    }
                    Text("Connect a storage folder", color = NispaxText, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    Text("Choose internal shared storage or an external USB folder. Android will ask you to grant access.", color = NispaxMuted)
                    Button(onClick = onChoose, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.FolderOpen, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Choose storage")
                    }
                }
            }
        }
        item { SectionTitle("Quick access") }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                QuickCard("Browse files", Icons.Default.Folder, Modifier.weight(1f), onBrowse)
                QuickCard("Storage", Icons.Default.DonutLarge, Modifier.weight(1f), onChoose)
            }
        }
        item { SectionTitle("Explore by type") }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                CategoryRow("Images", Icons.Default.Image)
                CategoryRow("Videos", Icons.Default.VideoLibrary)
                CategoryRow("Audio", Icons.Default.AudioFile)
                CategoryRow("Documents", Icons.Default.Description)
                CategoryRow("Downloads", Icons.Default.Download)
            }
        }
        item {
            Text("Nispax is offline-first. Your files stay on your device.", color = NispaxMuted, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 12.dp))
        }
    }
}

@Composable
private fun QuickCard(title: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Card(modifier.clickable(onClick = onClick), colors = CardDefaults.cardColors(containerColor = NispaxSurfaceAlt), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(icon, null, tint = NispaxBlue)
            Text(title, color = NispaxText, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(title, color = NispaxText, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
}

@Composable
private fun CategoryRow(title: String, icon: ImageVector) {
    Row(Modifier.fillMaxWidth().background(NispaxSurface, RoundedCornerShape(14.dp)).padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = NispaxBlue)
        Spacer(Modifier.width(12.dp))
        Text(title, color = NispaxText)
        Spacer(Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, null, tint = NispaxMuted)
    }
}

@Composable
private fun BrowseScreen(folder: DocumentFile?, onChoose: () -> Unit, onOpen: (DocumentFile) -> Unit) {
    ScreenHeader("Browse", "Internal and removable storage")
    if (folder == null) {
        EmptyState("No folder selected", "Choose a folder to start browsing files.", "Choose folder", onChoose)
    } else {
        val children = remember(folder.uri) { runCatching { folder.listFiles().sortedWith(compareByDescending<DocumentFile> { it.isDirectory }.thenBy { it.name?.lowercase() ?: "" }) }.getOrDefault(emptyList()) }
        Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(folder.name ?: "Selected folder", color = NispaxText, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            TextButton(onClick = onChoose) { Text("Change") }
        }
        if (children.isEmpty()) {
            EmptyState("This folder is empty", "Choose another folder or add files to this location.", "Choose folder", onChoose)
        } else {
            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                items(children, key = { it.uri.toString() }) { file ->
                    Row(
                        Modifier.fillMaxWidth().background(NispaxSurface, RoundedCornerShape(12.dp))
                            .clickable { if (file.isDirectory) onOpen(file) else runCatching {
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    setDataAndType(file.uri, file.type ?: "*/*")
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                // Launching is handled by the host activity context in the next iteration.
                            } }.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(if (file.isDirectory) Icons.Default.Folder else Icons.Default.InsertDriveFile, null, tint = NispaxBlue)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(file.name ?: "Unnamed", color = NispaxText, maxLines = 1)
                            Text(if (file.isDirectory) "Folder" else formatBytes(file.length()), color = NispaxMuted, style = MaterialTheme.typography.labelSmall)
                        }
                        Icon(Icons.Default.ChevronRight, null, tint = NispaxMuted)
                    }
                }
            }
        }
    }
}

@Composable
private fun StorageScreen(onChoose: () -> Unit) {
    ScreenHeader("Storage", "Manage connected storage locations")
    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(colors = CardDefaults.cardColors(containerColor = NispaxSurface)) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.SdStorage, null, tint = NispaxBlue)
                Text("Storage locations", color = NispaxText, style = MaterialTheme.typography.titleLarge)
                Text("Android controls access to shared and removable storage. Select a folder on each device to browse it.", color = NispaxMuted)
                Button(onClick = onChoose, modifier = Modifier.fillMaxWidth()) { Text("Connect storage folder") }
            }
        }
        Text("Storage analytics will be enabled after a location is connected.", color = NispaxMuted)
    }
}

@Composable
private fun ToolsScreen() {
    ScreenHeader("Tools", "Utilities for your files")
    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        listOf("Archive manager", "Duplicate finder", "Recycle Bin", "File checksum", "Private vault").forEach {
            Row(Modifier.fillMaxWidth().background(NispaxSurface, RoundedCornerShape(14.dp)).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Build, null, tint = NispaxBlue)
                Spacer(Modifier.width(12.dp))
                Text(it, color = NispaxText)
                Spacer(Modifier.weight(1f))
                Text("Coming soon", color = NispaxMuted, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun SettingsScreen() {
    ScreenHeader("Settings", "Make Nispax yours")
    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        listOf("Appearance · Black + Titanium + Electric Blue", "Default view · List", "Show hidden files · Off", "Privacy · Offline-first", "About · Nispax File Explorer 1.0.0-alpha01").forEach {
            Text(it, color = NispaxText, modifier = Modifier.fillMaxWidth().background(NispaxSurface, RoundedCornerShape(14.dp)).padding(16.dp))
        }
    }
}

@Composable
private fun EmptyState(title: String, message: String, action: String, onClick: () -> Unit) {
    Column(Modifier.fillMaxWidth().padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Icon(Icons.Default.FolderOpen, null, tint = NispaxBlue, modifier = Modifier.size(42.dp))
        Text(title, color = NispaxText, style = MaterialTheme.typography.titleMedium)
        Text(message, color = NispaxMuted)
        Button(onClick = onClick) { Text(action) }
    }
}

private fun formatBytes(bytes: Long): String {
    if (bytes < 1024) return "$bytes B"
    val units = listOf("KB", "MB", "GB", "TB")
    var value = bytes.toDouble()
    var index = -1
    do { value /= 1024.0; index++ } while (value >= 1024 && index < units.lastIndex)
    return DecimalFormat("#,##0.#").format(value) + " " + units[index]
}
