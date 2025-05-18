package de.yanneckreiss.mlkittutorial.ui.history

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import de.yanneckreiss.mlkittutorial.data.HistoryEntry
import de.yanneckreiss.mlkittutorial.viewmodel.HistoryViewModel

@SuppressLint("ComposeModifierMissing")
@Composable
fun HistoryScreen(
    history:   List<HistoryEntry>,
    onBack:    () -> Unit,
    historyVm: HistoryViewModel
) {
    val ctx       = LocalContext.current
    val clipboard = LocalClipboardManager.current

    // Dialog state
    var selectedEntry by remember { mutableStateOf<HistoryEntry?>(null) }
    var draftText     by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Captured History") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (history.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No captures yet", color = Color.Gray)
            }
        } else {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(history) { entry ->
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                selectedEntry = entry
                                draftText = entry.text
                            },
                        elevation = 4.dp
                    ) {
                        Row(
                            Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SelectionContainer {
                                Text(
                                    text = entry.text,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            IconButton(onClick = {
                                clipboard.setText(AnnotatedString(entry.text))
                                Toast.makeText(ctx, "Copied!", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                            }
                            IconButton(onClick = {
                                selectedEntry = entry
                                draftText = entry.text
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit")
                            }
                        }
                    }
                }
            }
        }

        selectedEntry?.let { entry ->
            AlertDialog(
                onDismissRequest = { selectedEntry = null },
                title = { Text("Edit & View") },
                text = {
                    OutlinedTextField(
                        value = draftText,
                        onValueChange = { draftText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp),
                        textStyle = LocalTextStyle.current.copy(color = Color.Black)
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        historyVm.update(entry.copy(text = draftText))
                        selectedEntry = null
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    Row {
                        IconButton(onClick = {
                            clipboard.setText(AnnotatedString(draftText))
                            Toast.makeText(ctx, "Copied!", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                        }
                        TextButton(onClick = { selectedEntry = null }) {
                            Text("Close")
                        }
                    }
                }
            )
        }
    }
}
