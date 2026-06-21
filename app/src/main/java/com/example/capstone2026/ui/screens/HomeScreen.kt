package com.example.capstone2026.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.capstone2026.R
import com.example.capstone2026.data.CalendarEvent
import com.example.capstone2026.util.cleanedEventTitle
import com.example.capstone2026.util.formatEventDate
import com.example.capstone2026.util.toLocalDate
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.LocalTime
import com.example.capstone2026.ui.components.QuickStatCard

/**
 * Main dashboard screen.
 * Shows current date, upcoming events, and navigation to other views.
 */
@Composable
fun HomeScreen(
    allEvents: SnapshotStateList<CalendarEvent>,
    onNavigateToUpload: () -> Unit,
    onNavigateToDaily: () -> Unit,
    onNavigateToWeekly: () -> Unit,
    onNavigateToMonthly: () -> Unit,
    navController: NavController
) {
    val user = FirebaseAuth.getInstance().currentUser
    val today = LocalDate.now()
    val formattedDate = today.format(
        DateTimeFormatter.ofPattern("EEEE, MMMM d")
    )
    val currentHour = LocalTime.now().hour
    val greeting = when {
        currentHour < 12 -> "Good morning"
        currentHour < 17 -> "Good afternoon"
        else -> "Good evening"
    }


    val upcomingEvents = allEvents
        .filter { it.start.toLocalDate() >= today }
        .sortedBy { it.start }
        .take(3)

    val eventsToday = allEvents.count {
        it.start.toLocalDate() == today
    }

    val assignmentsUpcoming = allEvents.count {
        it.start.toLocalDate() >= today && it.eventType?.lowercase()?.contains("assignment") == true
    }

    val examsThisWeek = allEvents.count {
        val eventDate = it.start.toLocalDate()
        eventDate >= today &&
                eventDate <= today.plusDays(7) &&
                it.eventType?.lowercase()?.contains("exam") == true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                .height(56.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "$greeting, ${user?.displayName ?: "Student"}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =  Arrangement.spacedBy(8.dp)
            ) {
                QuickStatCard(
                    label = "Today",
                    value = eventsToday.toString(),
                    modifier = Modifier.weight(1f)
                )
                QuickStatCard(
                    label = "Assignments",
                    value = assignmentsUpcoming.toString(),
                    modifier = Modifier.weight(1f)
                )
                QuickStatCard(
                    label = "Exams",
                    value = examsThisWeek.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
            val nextDeadline = allEvents
                .filter { event ->
                    val type = event.eventType?.lowercase().orEmpty()
                    event.start.toLocalDate() >= today &&
                            (type.contains("assignment") ||
                                    type.contains("exam") ||
                                    type.contains("project") ||
                                    event.title.lowercase().contains("due"))
                }
                .sortedBy { it.start }
                .firstOrNull()

            nextDeadline?.let { event ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val eventDate = event.start.toLocalDate()
                            navController.navigate("schedule_daily/$eventDate")
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Next Deadline",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Text(
                            text = cleanedEventTitle(event.title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        event.courseTitle?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        Text(
                            text = formatEventDate(event),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Text(
                text = "Today's Agenda",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (upcomingEvents.isEmpty()) {
                Text("No upcoming events")
            } else {
                upcomingEvents.forEach { event ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val eventDate = event.start.toLocalDate()
                                navController.navigate("schedule_daily/$eventDate")
                            }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = cleanedEventTitle(event.title),
                            fontWeight = FontWeight.Bold)
                            event.courseTitle?.let {
                                Text(
                                    text = it,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(text = formatEventDate(event)
                            ,
                            fontSize = 14.sp)
                            if (!event.notes.isNullOrBlank()) {
                                    Text("Notes: " + event.notes)
                            }
                        }
                    }
                }
            }
        }
    }
}