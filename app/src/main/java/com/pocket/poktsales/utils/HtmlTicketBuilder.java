package com.pocket.poktsales.utils;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by MAV1GA on 09/02/2018.
 */

public class HtmlTicketBuilder {

    public static Spanned buildTicket(){
        return Html.fromHtml(
                "<html lang=\"es\">\n" +
                        "  <head>\n" +
                        "    <style>\n" +
                        "    body{font-family: \"Verdana\", sans-serif; line-height: 0.8; width:600px;\n" +
                        "     margin: 0 auto}\n" +
                        "    h3 {color:#1c2430}\n" +
                        "    th{border-bottom: 1px solid #1c2430; font-size : 8px}\n" +
                        "    td{text-align:center}\n" +
                        "    </style>\n" +
                        "    <title></title>\n" +
                        "    <!-- Required meta tags -->\n" +
                        "    <meta charset=\"utf-8\">\n" +
                        "  <body>\n" +
                        "    <h3 style=\"text-align:center\">Company name</h3>\n" +
                        "    <hr style=\"display:block; width:100%; height:2px; background:#1c2430\"/>\n" +
                        "    <h4 style=\"text-align:center\">Gracias por su compra</h4>\n" +
                        "    <p style=\"text-align:center\">Este es el domicilio</p>\n" +
                        "    <p style=\"text-align:center\">Aqui va el RFC</p>\n" +
                        "    <p style=\"text-align:center\">Aqui va el telefono</p>\n" +
                        "    <hr style=\"display:block; width:100%; height:2px; background:#1c2430\"/>\n" +
                        "    <h5>Ticket de compra</h5>\n" +
                        "    <table style=\"width:100%\">\n" +
                        "      <tr>\n" +
                        "        <th>Cant.</th>\n" +
                        "        <th>Producto</th>\n" +
                        "        <th>Total</th>\n" +
                        "      </tr>\n" +
                        "      <tr>\n" +
                        "        <td/>\n" +
                        "        <td/>\n" +
                        "        <td style=\"border-top:1px solid black\"><h4>$ 23.50</h4></td>\n" +
                        "      </tr>\n" +
                        "    </table>\n" +
                        "    <p>Este no es un comprobante fiscal</p>\n" +
                        "    <hr style=\"display:block; width:100%; height:1px; background:#1c2430\"/>\n" +
                        "    <p style=\"color:#d3d3d3; text-align:center\">Creado con:</p>\n" +
                        "    <p style=\"text-align:center\">\n" +
                        "      <a href=\"https://goo.gl/P4g2pM\"><img src='https://i.imgur.com/qqhbC5K.jpg'/></a>\n" +
                        "    </p>\n" +
                        "  </body>\n" +
                        "</html>"
        );
    }

}
