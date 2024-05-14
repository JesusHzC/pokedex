package com.github.jesushzc.pokedex.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.github.jesushzc.pokedex.utils.clearFocusOnKeyboardDismiss

@Composable
fun SearchTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    onSearch: () -> Unit,
    placeholder: String? = null,
    modifier: Modifier = Modifier
) {

    OutlinedTextField(
        value = text,
        onValueChange = onTextChanged,
        shape = RoundedCornerShape(50),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
                defaultKeyboardAction(ImeAction.Search)
            }
        ),
        modifier = modifier
            .clearFocusOnKeyboardDismiss(),
        placeholder = {
            placeholder?.let {
                Text(text = it)
            }
        }
    )

}

@Preview
@Composable
private fun SearchTextFieldPreview() {
    SearchTextField(
        text = "",
        onTextChanged = {},
        onSearch = {}
    )
}
