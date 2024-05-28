//const MiniCssExtractPlugin = require("mini-css-extract-plugin");
//const devMode = process.env.NODE_ENV !== "production";
const HtmlWebpackPlugin = require('html-webpack-plugin');
const webpack = require('webpack'); //to access built-in plugins
/*export const module = {
    rules: [
        {
            test: /\.css$/i,
            use: ["style-loader", "css-loader"],
        },
    ],
};
*/
module.exports = {
    module: {
      rules: [{ test: /\.css$/, use: 'css-loader' }],
    },
    plugins: [new HtmlWebpackPlugin({ template: './src/index.html' })],
  };