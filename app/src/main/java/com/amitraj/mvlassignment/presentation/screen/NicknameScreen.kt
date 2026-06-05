package com.amitraj.mvlassignment.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amitraj.mvlassignment.presentation.viewmodel.NicknameViewModel
import com.amitraj.mvlassignment.ui.theme.*

@Composable
fun NicknameScreen(
    viewModel: NicknameViewModel,
    address: String,
    latitude: Double,
    longitude: Double,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initializeWithAddress(address, latitude, longitude)
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(id = com.amitraj.mvlassignment.R.string.title_set_nickname), fontSize = 24.sp)

        // Address display
        Text(stringResource(id = com.amitraj.mvlassignment.R.string.label_location, uiState.address), fontSize = 14.sp)

        // Nickname input
        TextField(
            value = uiState.nickname,
            onValueChange = { viewModel.updateNickname(it) },
            label = { Text(stringResource(id = com.amitraj.mvlassignment.R.string.label_nickname_input)) },
            modifier = Modifier.fillMaxWidth()
        )

        Text("${uiState.nickname.length}/20", fontSize = 12.sp)

        // Error display
        if (uiState.error != null) {
            Text(uiState.error!!, color = Color.Red, fontSize = 12.sp)
        }

        // Save button
        Button(
            onClick = { viewModel.saveNickname(latitude, longitude) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isSaving,
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = CtaGold,
                contentColor = Charcoal
            )
        ) {
            Text(
                text = if (uiState.isSaving) stringResource(id = com.amitraj.mvlassignment.R.string.btn_saving) else stringResource(id = com.amitraj.mvlassignment.R.string.btn_save),
                fontFamily = com.amitraj.mvlassignment.ui.theme.Pretendard,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.sp
            )
        }

        // Back button
        Button(
            onClick = onNavigateBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = com.amitraj.mvlassignment.R.string.btn_back))
        }
    }
}
