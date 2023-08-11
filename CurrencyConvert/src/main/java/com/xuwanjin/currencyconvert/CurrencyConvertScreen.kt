package com.xuwanjin.currencyconvert

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.xuwanjin.datastore.AppUtils
import com.xuwanjin.uicomponent.BaseScaffold
import com.xuwanjin.uicomponent.BaseTextField
import com.xuwanjin.uicomponent.RoundedCornerShape12

@RootNavGraph(start = true)
@Destination
@Preview
@Composable
fun CurrencyConvertScreen(
    viewModel: MainViewModel = hiltViewModel<MainViewModel>(),
    ) {
    val inputValue = remember {
        mutableDoubleStateOf(1.0)
    }

    val isExpanded = remember {
        mutableStateOf(false)
    }
    val currencyConvertUiState by viewModel.currencyConvertUiState.collectAsState()
    val selectedCurrency = remember {
        mutableStateOf("USD")
    }
    BaseScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
        ,
        topBarLayout = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                BaseTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape12),
                    inputValue,
                    onValueChange = {
                        try {
                            inputValue.value = it.toDouble()
                            viewModel.onBaseCurrencyChange(it.toDouble(), selectedCurrency.value)
                        } catch (exception: NumberFormatException) {

                        }
                    }
                )
                CurrencyDropdownMenu(
                    isExpanded = isExpanded,
                    selectedCurrency = selectedCurrency,
                    currencyConvertUiState = currencyConvertUiState,
                    onBaseCurrencyChange = {
                        viewModel.onBaseCurrencyChange(inputValue.value, it)
                    }
                )

            }
        },
        content = {
            when (currencyConvertUiState) {
                is CurrencyConvertUiState.Success -> {
                    val ratesPairList =
                        (currencyConvertUiState as CurrencyConvertUiState.Success).currencyData?.ratesMap?.toList()
                    LazyVerticalGrid(
                        modifier = Modifier.padding(top = 20.dp),
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        content = {
                            if (!ratesPairList.isNullOrEmpty()){
                                items(ratesPairList.size) { index: Int ->
                                    Column(
                                        modifier = Modifier
                                            .height(80.dp)
                                            .background(Color.Gray)
                                    ) {
                                        Text(
                                            modifier = Modifier.weight(1.0f),
                                            text = String.format(
                                                "%.3f",
                                                (ratesPairList[index].second)
                                            ),
                                            style = TextStyle(
                                                color = Color.Green,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 20.sp,
                                            ),
                                            maxLines = 2,
                                        )
                                        Text(
                                            text = ratesPairList[index].first,
                                            style = TextStyle(
                                                color = Color.Green,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 12.sp,
                                            )
                                        )

                                    }
                                }
                            }

                        }
                    )

                }

                else -> {

                }
            }
        },
    )
}

@Composable
private fun ColumnScope.CurrencyDropdownMenu(
    isExpanded: MutableState<Boolean>,
    selectedCurrency: MutableState<String>,
    currencyConvertUiState: CurrencyConvertUiState,
    onBaseCurrencyChange: (String) -> Unit,
) {
    Box(
        modifier = Modifier

            .align(Alignment.End),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            when (currencyConvertUiState) {
                is CurrencyConvertUiState.Success -> {
                    val date =
                        AppUtils.timestampToLocalDate(currencyConvertUiState.currencyData?.timestamp?:0)
                    Text(
                        modifier = Modifier,
                        text = "data updated time: $date",
                    )
                }

                else -> {

                }
            }
            Spacer(modifier = Modifier.weight(1.0f))
            Box {
                IconButton(
                    modifier = Modifier.background(Color.Gray),
                    onClick = {
                        isExpanded.value = !isExpanded.value
                    }
                ) {
                    Row {
                        Text(
                            text = selectedCurrency.value,
                            style = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Medium,
                                fontSize = 17.sp,
                            )
                        )
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = ""
                        )
                    }
                }
                DropdownMenu(
                    modifier = Modifier.height(300.dp),
                    expanded = isExpanded.value,
                    onDismissRequest = {
                        isExpanded.value = false
                    }
                ) {
                    when (currencyConvertUiState) {
                        is CurrencyConvertUiState.Success -> {
                            val keys =
                                currencyConvertUiState.currencyData?.ratesMap?.keys?.toList()
                            keys?.forEachIndexed { index, _ ->
                                val item = keys[index]
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        selectedCurrency.value = item
                                        isExpanded.value = false
                                        onBaseCurrencyChange(item)
                                    }
                                )
                            }
                        }

                        else -> {

                        }
                    }
                }
            }
        }

    }
}
