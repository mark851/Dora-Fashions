package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BronzeBrown
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.WhatsAppGreen
import java.net.URLEncoder

const val DORA_EMAIL = "Namatakadoreen89@gmail.com"

@Composable
fun ContactScreen(
    onSendMessage: (name: String, contact: String, message: String) -> Unit,
    onOpenDomainDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var contactInfo by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Get in Touch with Dora Fashions",
            style = MaterialTheme.typography.headlineSmall,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = BronzeBrown
        )
        Text(
            text = "We're here for custom bead designs, wholesale supplies, orders, and questions.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Contact Channels Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ContactRow(
                    icon = Icons.Default.Phone,
                    title = "WhatsApp & Phone",
                    subtitle = "+256 775 803 896",
                    tint = WhatsAppGreen,
                    onClick = {
                        val url = "https://wa.me/$WHATSAPP_PHONE?text=${URLEncoder.encode("Hello Dora Fashions, I have an inquiry about beads.", "UTF-8")}"
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    }
                )

                ContactRow(
                    icon = Icons.Default.Email,
                    title = "Email",
                    subtitle = DORA_EMAIL,
                    tint = GoldPrimary,
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:$DORA_EMAIL")
                            putExtra(Intent.EXTRA_SUBJECT, "Dora Fashions Bead Inquiry")
                        }
                        context.startActivity(intent)
                    }
                )

                ContactRow(
                    icon = Icons.Default.LocationOn,
                    title = "Store Location",
                    subtitle = "Kampala, Uganda (Delivery nationwide)",
                    tint = BronzeBrown,
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=Kampala+Uganda"))
                        context.startActivity(intent)
                    }
                )

                ContactRow(
                    icon = Icons.Default.Schedule,
                    title = "Opening Hours",
                    subtitle = "Mon–Sat: 8:00 AM – 7:00 PM\nSun: 10:00 AM – 4:00 PM",
                    tint = GoldPrimary,
                    onClick = {}
                )

                ContactRow(
                    icon = Icons.Default.Language,
                    title = "Online Web Store Domain",
                    subtitle = "Accessible to visitors worldwide · Tap to view link",
                    tint = BronzeBrown,
                    onClick = onOpenDomainDialog
                )
            }
        }

        // Send Inquiry Form (Saved into live DB)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Send a Message or Custom Order Request",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = BronzeBrown
                )
                Text(
                    text = "Your message will be saved to our live database and our team will get back to you.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Your Name *") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("contact_name_input")
                )

                OutlinedTextField(
                    value = contactInfo,
                    onValueChange = { contactInfo = it },
                    label = { Text("Phone Number or Email *") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("contact_phone_input")
                )

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Your Message or Custom Bead Design Details *") },
                    maxLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("contact_message_input")
                )

                Button(
                    onClick = {
                        if (name.isBlank() || contactInfo.isBlank() || message.isBlank()) {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        onSendMessage(name.trim(), contactInfo.trim(), message.trim())
                        Toast.makeText(context, "Message saved to live database! We will contact you soon.", Toast.LENGTH_LONG).show()
                        name = ""
                        contactInfo = ""
                        message = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("send_contact_message_button")
                ) {
                    Text("Send Message", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ContactRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(
                subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
        }
    }
}
