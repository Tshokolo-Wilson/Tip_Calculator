package com.example.tipcalculator

import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import java.text.NumberFormat
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material3.Switch
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.Icon
import java.security.Key


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalculatorTheme {
                Surface ()
                {
                    TipTimeLayout()
                }

            }
        }
    }
}

@Composable
fun TipTimeLayout() {
    var amountInput by remember { mutableStateOf("")  }
    var tipInput by remember { mutableStateOf("") }
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0

    var roundUp by remember { mutableStateOf(false) }

    val tip = calculateTip(amount,tipPercent,roundUp)


    Column (
        modifier = Modifier
            .padding(horizontal = 40.dp)
            . verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp , top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            label = R.string.bill_amount,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = amountInput,
            onValueChanged = {amountInput = it},
            leadingIcon = R.drawable.money,
            modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth())

        Spacer(modifier = Modifier.height(15.dp))

        EditNumberField(
            label = R.string.how_was_the_service,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = tipInput,
            onValueChanged = {tipInput= it},
            leadingIcon = R.drawable.percent,
            modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth()
        )

       Row(){
           RoundTheTipRow(
               roundUp = roundUp,
               onRoundUpChanged = {roundUp = it}
           )
       }



        Text(
            text = stringResource(R.string.tip_amount,tip),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(15.dp))
    }
}

private  fun calculateTip(amount:Double,tipPercent:Double = 15.0,roundUp: Boolean): String{
    var tip = tipPercent / 100 * amount
    if(roundUp) {
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Composable
fun EditNumberField(modifier: Modifier = Modifier,
                    value:String,
                    @StringRes label: Int,
                    @DrawableRes leadingIcon: Int,
                    keyboardOptions: KeyboardOptions,
                    onValueChanged:(String) -> Unit) {


    TextField(
        value = value,
        leadingIcon ={ Icon(painter= painterResource(id=leadingIcon),null) },
        onValueChange = onValueChanged,
        label = { Text (stringResource(label) ) },
        singleLine = true,
        modifier = Modifier,
        keyboardOptions = keyboardOptions

    )
}

@Composable
fun RoundTheTipRow(
    roundUp:Boolean,
    onRoundUpChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {

        Text(text = stringResource(R.string.round_up_tip))

        Switch(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,

        )
    }
}

@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview(){
    TipCalculatorTheme {
        TipTimeLayout()
    }
}



