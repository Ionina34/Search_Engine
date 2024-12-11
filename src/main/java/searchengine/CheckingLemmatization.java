package searchengine;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import searchengine.services.LemmaFinder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CheckingLemmatization {
    public static void main(String[] args) throws IOException {
        LuceneMorphology morphology = new RussianLuceneMorphology();
        List<String> wordBaseForms= morphology.getNormalForms("aрктический");
        wordBaseForms.forEach(System.out::println);

        System.out.println(morphology.getMorphInfo("арктический"));
        System.exit(1);

        LemmaFinder lemmaFinder = new LemmaFinder();
        String content = "\n" +
                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"><html><head>\n" +
                "<title>Корзина</title>\n" +
                "<meta name=\"description\" content=\"Продажа по доступным ценам. PlayBack.ru - Интернет-Магазин - Большой выбор смартфонов, планшетов, носимой электроники по низким ценам, отличный сервис, гарантии производителя\">\n" +
                "<meta name=\"keywords\" content=\"купить, цена, описание, интернет-магазин, интернет, магазин, продажа, смартфоны\">\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                "<meta http-equiv=\"Last-Modified\" content=\"Mon, 08 Nov 2021 16:45:40 GMT\">\n" +
                "<link rel=\"shortcut icon\" href=\"/favicon.ico\">\n" +
                "<link rel=\"apple-touch-icon\" href=\"/logo_apple.png\">\n" +
                "<link rel=\"StyleSheet\" href=\"/include_new/styles.css\" type=\"text/css\" media=\"all\">\n" +
                "<script type=\"text/javascript\" src=\"/FD126C42-EBFA-4E12-B309-BB3FDD723AC1/main.js?attr=rF5ZQE9BCWZtlhwt1SnK-yeJLUS4KJ6pNTX3c9TF0h3k-vNZnE1xjcYTuqZsZa_2pEQUVbox2lMKa0hLrwsncg\" charset=\"UTF-8\"></script></head>\n" +
                "\n" +
                "<link rel=\"stylesheet\" href=\"/include_new/jquery-ui.css\" />\n" +
                "<!--<link rel=\"stylesheet\" href=\"http://code.jquery.com/ui/1.10.0/themes/base/jquery-ui.css\" />-->\n" +
                "\t<script src=\"https://code.jquery.com/jquery-1.8.3.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"/jscripts/jquery.easing.1.3.js\"></script>\n" +
                "\t<script src=\"https://code.jquery.com/ui/1.10.0/jquery-ui.js\"></script>\n" +
                "    <script type=\"text/javascript\" src=\"/jscripts/menuscript.js\"></script>\n" +
                "\t<script src=\"/jscripts/jquery.inputmask.js\" type=\"text/javascript\"></script>\n" +
                "\t<script src=\"/jscripts/jquery.inputmask.extensions.js\" type=\"text/javascript\"></script>\n" +
                "\t<script src=\"/jscripts/jquery.inputmask.numeric.extensions.js\" type=\"text/javascript\"></script>\n" +
                "\t<link rel=\"stylesheet\" type=\"text/css\" href=\"/fancybox/jquery.fancybox-1.3.4.css\" media=\"screen\" />\n" +
                "<script type=\"text/javascript\" src=\"/fancybox/jquery.mousewheel-3.0.4.pack.js\"></script>\n" +
                "\t<script type=\"text/javascript\" src=\"/fancybox/jquery.fancybox-1.3.4.js\"></script>\n" +
                "\t\t\t\t<script type=\"text/javascript\">\n" +
                "\t\t\t\t$(document).ready(function() {\n" +
                "\t\t\t\t$(\"a[rel=biggallery]\").fancybox(\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t'opacity'\t\t: 'true',\n" +
                "\t\t\t\t'overlayShow'\t: 'false',\n" +
                "\t\t\t\t'transitionIn'\t: 'elastic',\n" +
                "             \t'transitionOut'\t: 'elastic',\n" +
                "\t\t\t\t'overlayShow'\t: 'true',\n" +
                "\t\t\t\t'centerOnScroll'\t: 'true',\n" +
                "\t\t\t\t}); \n" +
                "\t\t\t\t$(\"a.examplefoto\").fancybox(\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t'opacity'\t\t: 'true',\n" +
                "\t\t\t\t'overlayShow'\t: 'false',\n" +
                "\t\t\t\t'transitionIn'\t: 'elastic',\n" +
                "             \t'transitionOut'\t: 'elastic',\n" +
                "\t\t\t\t'overlayShow'\t: 'true'\n" +
                "\t\t\t\t});\n" +
                "\t\t\t$(\"a.getaccinfo\").fancybox({\n" +
                "\t\t\t    'opacity'\t\t\t\t: true,\n" +
                "\t\t\t\t'width'\t\t\t\t    : 1224,\n" +
                "\t\t\t\t'height'\t\t    \t: 700,\n" +
                "\t\t\t\t'maxHeight'\t\t\t\t: 700,\n" +
                "\t\t\t    'fitToView'      \t\t: 'false',\n" +
                "\t\t\t\t'autoScale'\t\t\t    : 'false',\n" +
                "\t\t\t\t'type'\t\t\t\t    : 'iframe',\n" +
                "\t\t\t\t'transitionIn'\t\t\t: 'elastic',\n" +
                "             \t'transitionOut'\t\t\t: 'elastic',\n" +
                "\t\t\t\t onClosed : function(){ \n" +
                "\t\t\t\t\tvar xmlhttp = getXmlHttp();\n" +
                "\t\t\t\t\txmlhttp.open('GET', '/include_new/get_basket_sost.php', false);\n" +
                "\t\t\t\t\txmlhttp.send(null);\n" +
                "\t\t\t\t\tif(xmlhttp.status == 200) {\n" +
                "\t\t\t\t\tvar xx = document.getElementById('sosotoyaniekorziny');\n" +
                "\t\t\t\t\txx.innerHTML=xmlhttp.responseText;\n" +
                "\t\t\t\t\t} }\n" +
                "\t\t\t});\n" +
                "\t\t\t$(\"a.getaccinfoss\").fancybox({\n" +
                "\t\t\t    'opacity'\t\t\t\t: true,\n" +
                "\t\t\t\t'width'\t\t\t\t    : 1224,\n" +
                "\t\t\t\t'height'\t\t    \t: 700,\n" +
                "\t\t\t\t'maxHeight'\t\t\t\t: 700,\n" +
                "\t\t\t    'fitToView'      \t\t: 'false',\n" +
                "\t\t\t\t'autoScale'\t\t\t    : 'false',\n" +
                "\t\t\t\t'type'\t\t\t\t    : 'iframe',\n" +
                "\t\t\t\t'transitionIn'\t\t\t: 'elastic',\n" +
                "             \t'transitionOut'\t\t\t: 'elastic',\n" +
                "\t\t\t\t onClosed : function(){ \n" +
                "\t\t\t\t\t\t\tloadbasket(curexch);\n" +
                "\t\t\t\t\t\t\tgetprodlisting();\n" +
                "\t\t\t\t\t\t\tloaddelivery();\n" +
                "\t\t\t\t\t\t\tgetsumall();\n" +
                "\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t }\n" +
                "\t\t\t});\n" +
                "\t\t\t$(\"a.getaccinfosse\").fancybox({\n" +
                "\t\t\t    'opacity'\t\t\t\t: true,\n" +
                "\t\t\t\t'width'\t\t\t\t    : 1224,\n" +
                "\t\t\t\t'height'\t\t    \t: 700,\n" +
                "\t\t\t\t'maxHeight'\t\t\t\t: 700,\n" +
                "\t\t\t    'fitToView'      \t\t: 'false',\n" +
                "\t\t\t\t'autoScale'\t\t\t    : 'false',\n" +
                "\t\t\t\t'type'\t\t\t\t    : 'iframe',\n" +
                "\t\t\t\t'transitionIn'\t\t\t: 'elastic',\n" +
                "             \t'transitionOut'\t\t\t: 'elastic',\n" +
                "\t\t\t\t onClosed : function(){ \n" +
                "\t\t\t\t\t\t\tvar xmlhttp = getXmlHttp();\n" +
                "\t\t\t\t\t\t\txmlhttp.open('GET', '/include_new/get_basket_sost.php', false);\n" +
                "\t\t\t\t\t\t\txmlhttp.send(null);\n" +
                "\t\t\t\t\t\t\tif(xmlhttp.status == 200) {\n" +
                "\t\t\t\t\t\t\tvar xx = document.getElementById('sosotoyaniekorziny');\n" +
                "\t\t\t\t\t\t\txx.innerHTML=xmlhttp.responseText;\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\tloadprodactions();\n" +
                "\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t }\n" +
                "\t\t\t});\n" +
                "\t\t\t$(\"a.getcallback\").fancybox({\n" +
                "\t\t\t\t'type'\t\t\t\t: 'iframe',\n" +
                "\t\t\t\t'width'\t\t\t\t: 450,\n" +
                "\t\t\t\t'height'\t\t\t: 250,\n" +
                "\t\t\t\t'maxHeight'\t\t\t: 250,\n" +
                "\t\t\t\t'fitToView'      \t: 'false',\n" +
                "\t\t\t\t'autoScale'\t\t\t: 'false',\n" +
                "\t\t\t\t'openEffect'\t\t: 'elastic',\n" +
                "             \t'closeEffect'\t\t: 'elastic'\n" +
                "\t\t\t});\n" +
                "\t\t\t$(\"a.geaddrmag\").fancybox({\n" +
                "\t\t\t'type'\t\t\t\t: 'iframe',\n" +
                "\t\t\t'width'\t\t\t\t: 650,\n" +
                "\t\t\t'height'\t\t\t: 710,\n" +
                "\t\t\t'maxHeight'\t\t\t: 900,\n" +
                "\t\t\t'fitToView'      \t: 'false',\n" +
                "\t\t\t'autoScale'\t\t\t: 'false',\n" +
                "\t\t\t'openEffect'\t\t: 'elastic',\n" +
                "\t\t\t'closeEffect'\t\t: 'elastic',\n" +
                "\t\t\t'titleShow'\t\t\t: 'true',\n" +
                "\t\t\ttitlePosition\t\t: 'over'\n" +
                "\t\t\t});\n" +
                "\t\t\t$(\"a.getdelusl\").fancybox({\n" +
                "\t\t\t'type'\t\t\t\t: 'iframe',\n" +
                "\t\t\t'width'\t\t\t\t: 900,\n" +
                "\t\t\t'height'\t\t\t: 900,\n" +
                "\t\t\t'maxHeight'\t\t\t: 900,\n" +
                "\t\t\t'fitToView'      \t: 'false',\n" +
                "\t\t\t'autoScale'\t\t\t: 'false',\n" +
                "\t\t\t'openEffect'\t\t: 'elastic',\n" +
                "\t\t\t'closeEffect'\t\t: 'elastic'\n" +
                "\t\t\t});\n" +
                "\t\t\t\t});\n" +
                "\t\t\t\t\n" +
                "\t\t\t// Определяем типы сообщений^\n" +
                "var myMessages = ['success'];\n" +
                " \n" +
                "function hideAllMessages() {\n" +
                "\tvar messagesHeights = new Array();\n" +
                "\n" +
                "\tfor(i = 0; i < myMessages.length; i++) {\n" +
                "\t\tmessagesHeights[i] = $('.' + myMessages[i]).outerHeight();\n" +
                "\t\t$('.' + myMessages[i]).css('top', -messagesHeights[i]);\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "function showMessage(type) {\n" +
                "\t$('.' + type + '-trigger').click(function() {\n" +
                "\t\thideAllMessages();\n" +
                "\t\t$('.' + type).animate({top:'0'}, 500);\n" +
                "\t\t\n" +
                "\t});\n" +
                "}\n" +
                "function hideMessage() {\n" +
                "\t$('.success').animate({top: -$('.success').outerHeight()}, 500);\n" +
                "}\n" +
                "function loadMessage(type) {\n" +
                "\thideAllMessages();\n" +
                "\t$('.' + type).animate({top:'0'}, 500);\n" +
                "\tsetTimeout(\"hideMessage()\", 3000);\n" +
                "}\n" +
                "\n" +
                "$(document).ready(function() {\n" +
                "\thideAllMessages();\n" +
                " \n" +
                "\tfor(var i = 0; i < myMessages.length; i++) {\n" +
                "\t\tshowMessage(myMessages[i]);\n" +
                "\t}\n" +
                "\n" +
                "\t$('.message').click(function() {\n" +
                "\t\t$(this).animate({top: -$(this).outerHeight()}, 500);\n" +
                "\t});\t \n" +
                "\n" +
                "});       \n" +
                "\t\t\t\t\n" +
                "\t\t\t\tfunction getXmlHttp(){\n" +
                " \t\t\t\t\t var xmlhttp;\n" +
                " \t\t\t\t\t try {\n" +
                " \t\t\t\t\t   xmlhttp = new ActiveXObject(\"Msxml2.XMLHTTP\");\n" +
                "  \t\t\t\t\t} catch (e) {\n" +
                " \t\t\t\t\t   try {\n" +
                "   \t\t\t\t\t   xmlhttp = new ActiveXObject(\"Microsoft.XMLHTTP\");\n" +
                "   \t\t\t\t\t } catch (E) {\n" +
                "   \t\t\t\t\t   xmlhttp = false;\n" +
                "  \t\t\t\t\t  }\n" +
                " \t\t\t\t\t }\n" +
                "  \t\t\t\t\tif (!xmlhttp && typeof XMLHttpRequest!='undefined') {\n" +
                "  \t\t\t\t\t  xmlhttp = new XMLHttpRequest();\n" +
                "  \t\t\t\t\t\t}\n" +
                " \t\t\t\t\t return xmlhttp;\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\tfunction addtobasket(gettprodid,productcurname)\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\tvar xmlhttp = getXmlHttp();\n" +
                "\t\t\t\t\txmlhttp.open('GET', '/basket_add.php?action=1&good_id='+gettprodid, false);\n" +
                "\t\t\t\t\txmlhttp.send(null);\n" +
                "\t\t\t\t\tif(xmlhttp.status == 200) {\n" +
                "  \t\t\t\t\txmlhttp.abort();\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t\txmlhttp.open('GET', '/include_new/get_basket_sost.php', false);\n" +
                "\t\t\t\t\txmlhttp.send(null);\n" +
                "\t\t\t\t\tvar xxm = document.getElementById('name_added_tov');\n" +
                "\t\t\t\t\txxm.innerHTML=productcurname;\n" +
                "\t\t\t\t\tloadMessage('success');\n" +
                "\t\t\t\t\tif(xmlhttp.status == 200) {\n" +
                "\t\t\t\t\tvar xx = document.getElementById('sosotoyaniekorziny');\n" +
                "\t\t\t\t\txx.innerHTML=xmlhttp.responseText;\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t\tdocument.getElementById('buyimg'+gettprodid).innerHTML=\"В корзине\";\n" +
                "\t\t\t\t\tdocument.getElementById('buyimg'+gettprodid).className=\"buyeditem\";\n" +
                "\t\t\t\t\tdocument.getElementById('buyimg'+gettprodid).onclick=function(){\t};\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t\tfunction addtobasketacc(gettprodid,productcurname)\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\tvar xmlhttp = getXmlHttp();\n" +
                "\t\t\t\t\txmlhttp.open('GET', '/basket_add.php?action=1&good_id='+gettprodid, false);\n" +
                "\t\t\t\t\txmlhttp.send(null);\n" +
                "\t\t\t\t\tif(xmlhttp.status == 200) {\n" +
                "  \t\t\t\t\txmlhttp.abort();\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t\tvar xxm = document.getElementById('name_added_tov');\n" +
                "\t\t\t\t\txxm.innerHTML=productcurname;\n" +
                "\t\t\t\t\tloadMessage('success');\n" +
                "\t\t\t\t\tdocument.getElementById('buyimg'+gettprodid).src=\"/img_new/buy_ok.png\";\n" +
                "\t\t\t\t\tdocument.getElementById('buyimg'+gettprodid).onclick=function(){\t};\n" +
                "\t\t\t\t\t}\n" +
                "var bprodmass=new Array();\n" +
                "var bprodlist = \"\";\n" +
                "bprodmass=bprodlist.split(',');\n" +
                "var exchange=1;\n" +
                "var curexch=1;\n" +
                "var rekvt=0;\n" +
                "var addrt=1;\n" +
                "var delivprice=390;\n" +
                "var dost=1;\n" +
                "function getprodlisting()\n" +
                "{ \n" +
                "var xmlhttp = getXmlHttp();\n" +
                "xmlhttp.open('GET', '/getdata/get_basket.php', false);\n" +
                "xmlhttp.send(null);\n" +
                "if(xmlhttp.status == 200) \n" +
                "\t{\n" +
                "\t\tbprodlist=xmlhttp.responseText;\n" +
                "\t\tbprodmass=bprodlist.split(',');\n" +
                "\t} \n" +
                "}\n" +
                "function getsumprod(prodid)\n" +
                "{\n" +
                "\tvar summa=0;\n" +
                "\tvar i=0;\n" +
                "\tvar curecena;\n" +
                "\tvar curecenat;\n" +
                "\tvar curcount;\n" +
                "\tvar cursumm;\n" +
                "\tvar prwithe=0;\n" +
                "\t\tcurecenat=document.getElementById('pricec'+prodid);\n" +
                "\t\tcurecena=document.getElementById('price'+prodid);\n" +
                "\t\tcurcount=document.getElementById('quant'+prodid);\n" +
                "\t\tcursumm=document.getElementById('summ'+prodid);\n" +
                "\t\tcursummtext=document.getElementById('summs'+prodid);\n" +
                "\t\tprwithe=Math.ceil(eval(curecena.value)*eval(exchange));\n" +
                "\t\tcurecenat.innerHTML=prwithe;\n" +
                "\t\tsumma=Math.ceil(eval(prwithe)*eval(curcount.value));\n" +
                "\t\tmybasketset(prodid, curcount.value);\n" +
                "\t\tcursumm.value=summa;\n" +
                "\t\tcursummtext.innerHTML=summa;\n" +
                "}\n" +
                "function getsumall()\n" +
                "{\n" +
                "\tloadactions();\n" +
                "\t$(\"a.getaccinfoss\").fancybox({\n" +
                "\t\t\t    'opacity'\t\t\t\t: true,\n" +
                "\t\t\t\t'width'\t\t\t\t    : 1224,\n" +
                "\t\t\t\t'height'\t\t    \t: 700,\n" +
                "\t\t\t\t'maxHeight'\t\t\t\t: 700,\n" +
                "\t\t\t    'fitToView'      \t\t: 'false',\n" +
                "\t\t\t\t'autoScale'\t\t\t    : 'false',\n" +
                "\t\t\t\t'type'\t\t\t\t    : 'iframe',\n" +
                "\t\t\t\t'transitionIn'\t\t\t: 'elastic',\n" +
                "             \t'transitionOut'\t\t\t: 'elastic',\n" +
                "\t\t\t\t onClosed : function(){ \n" +
                "\t\t\t\t\t\t\tloadbasket(curexch);\n" +
                "\t\t\t\t\t\t\tgetprodlisting();\n" +
                "\t\t\t\t\t\t\tloaddelivery();\n" +
                "\t\t\t\t\t\t\tgetsumall();\n" +
                "\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t }\n" +
                "\t\t\t});\n" +
                "\tvar summa=0;\n" +
                "\tvar i=0;\n" +
                "\tvar curelem;\n" +
                "\tvar curelemtext=document.getElementById('fullsummtext');;\n" +
                "\tvar allsum=document.getElementById('fullsumm');\n" +
                "\tfor (i = 0; i < bprodmass.length; i++) {\n" +
                "\t\tif(document.getElementById('tablerow'+bprodmass[i]).length != 0 &&document.getElementById('tablerow'+bprodmass[i]) != 'none')\n" +
                "\t\t{\n" +
                "\t\tgetsumprod(bprodmass[i]);\n" +
                "\t\tcurelem=document.getElementById('summ'+bprodmass[i]);\n" +
                "\t\tsumma=summa+eval(curelem.value);\n" +
                "\t\t}\n" +
                "}\n" +
                "if(summa==0)\n" +
                "{\n" +
                "\tlocation.reload();\n" +
                "    return false;\n" +
                "}\n" +
                "var skidkaprice=document.getElementById('skidka');\n" +
                "summa=eval(summa)-eval(skidkaprice.value);\n" +
                "if(summa>150000)devfalse();\n" +
                "else \n" +
                "{\n" +
                "var delivra = document.getElementById('getfeliv');\n" +
                "delivra.disabled=false;\n" +
                "}\n" +
                "loaddelivery();\n" +
                "var delivprice=document.getElementById('delivsumm');\n" +
                "summa=eval(summa)+eval(delivprice.value);\n" +
                "allsum.value=summa;\n" +
                "curelemtext.innerHTML=summa;\n" +
                "}\n" +
                "\n" +
                "function getfullsum()\n" +
                "{\n" +
                "\tloadbasket(1);\n" +
                "\tloadactions();\n" +
                "\tloaddelivery();\n" +
                "\tvar summa=0;\n" +
                "\tvar i=0;\n" +
                "\tvar curelem;\n" +
                "\tvar curelemtext=document.getElementById('fullsummtext');;\n" +
                "\tvar allsum=document.getElementById('fullsumm');\n" +
                "\tfor (i = 0; i < bprodmass.length; i++) {\n" +
                "\t\tif(document.getElementById('tablerow'+bprodmass[i]).length != 0 &&document.getElementById('tablerow'+bprodmass[i]) != 'none')\n" +
                "\t\t{\n" +
                "\t\tgetsumprod(bprodmass[i]);\n" +
                "\t\tcurelem=document.getElementById('summ'+bprodmass[i]);\n" +
                "\t\tsumma=summa+eval(curelem.value);\n" +
                "\t\t}\n" +
                "}\n" +
                "var skidkaprice=document.getElementById('skidka');\n" +
                "summa=eval(summa)-eval(skidkaprice.value);\n" +
                "if(summa>50000)devfalse();\n" +
                "else yesdostavka();\n" +
                "var curdelivprice=document.getElementById('delivsumm');\n" +
                "summa=eval(summa)+eval(curdelivprice.value);\n" +
                "allsum.value=summa;\n" +
                "curelemtext.innerHTML=summa;\n" +
                "}\n" +
                "function mybasketset(prodid, quan)\n" +
                "{\n" +
                "\tvar xmlhttp = getXmlHttp();\n" +
                "\t\t\t\t\txmlhttp.open('GET', '/basket_add.php?action=5&good_id='+prodid+'&quantity='+quan, false);\n" +
                "\t\t\t\t\txmlhttp.send(null);\n" +
                "\t\t\t\t\tif(xmlhttp.status == 200) {\n" +
                "  \t\t\t\t\txmlhttp.abort();\n" +
                "\t\t\t\t\t}\n" +
                "}\n" +
                "function mybasketdel(prodid)\n" +
                "{\n" +
                "\tvar xmlhttp = getXmlHttp();\n" +
                "\t\t\t\t\txmlhttp.open('GET', '/basket_add.php?action=2&good_id='+prodid, false);\n" +
                "\t\t\t\t\txmlhttp.send(null);\n" +
                "\t\t\t\t\tif(xmlhttp.status == 200) {\n" +
                "  \t\t\t\t\txmlhttp.abort();\n" +
                "\t\t\t\t\t}\n" +
                "\tvar curcount=document.getElementById('quant'+prodid);\n" +
                "\tcurcount.value=0;\n" +
                "\tvar prodtr=document.getElementById('tablerow'+prodid);\n" +
                "\tprodtr.style.display='none';\n" +
                "\tgetsumall();\n" +
                "}\n" +
                "function loadbasket(exch)\n" +
                "{\n" +
                "\tvar curbasket=document.getElementById('productlist');\n" +
                "\tvar xmlhttp = getXmlHttp();\n" +
                "\t\t\t\t\txmlhttp.open('GET', '/getdata/get_product_list.php?ex='+exch, false);\n" +
                "\t\t\t\t\txmlhttp.send(null);\n" +
                "\t\t\t\t\tif(xmlhttp.status == 200) {\n" +
                "\t\t\t\t\tcurbasket.innerHTML=xmlhttp.responseText;\n" +
                "\t\t\t\t\t}\n" +
                "}\n" +
                "function loadactions()\n" +
                "{\n" +
                "\tvar curact=document.getElementById('actionspan');\n" +
                "\tvar xmlhttp = getXmlHttp();\n" +
                "\t\t\t\t\txmlhttp.open('GET', '/getdata/get_action.php', false);\n" +
                "\t\t\t\t\txmlhttp.send(null);\n" +
                "\t\t\t\t\tif(xmlhttp.status == 200) {\n" +
                "\t\t\t\t\tcuract.innerHTML=xmlhttp.responseText;\n" +
                "\t\t\t\t\t}\n" +
                "}\n" +
                "function loadprodactions(curproduct)\n" +
                "{\n" +
                "\tvar curact=document.getElementById('actionprodspan');\n" +
                "\tvar xmlhttp = getXmlHttp();\n" +
                "\t\t\t\t\txmlhttp.open('GET', '/getdata/get_action_product.php?getproductid='+curproduct, false);\n" +
                "\t\t\t\t\txmlhttp.send(null);\n" +
                "\t\t\t\t\tif(xmlhttp.status == 200) {\n" +
                "\t\t\t\t\tcuract.innerHTML=xmlhttp.responseText;\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t\t$(\"a.getaccinfosse\").fancybox({\n" +
                "\t\t\t    'opacity'\t\t\t\t: true,\n" +
                "\t\t\t\t'width'\t\t\t\t    : 1224,\n" +
                "\t\t\t\t'height'\t\t    \t: 700,\n" +
                "\t\t\t\t'maxHeight'\t\t\t\t: 700,\n" +
                "\t\t\t    'fitToView'      \t\t: 'false',\n" +
                "\t\t\t\t'autoScale'\t\t\t    : 'false',\n" +
                "\t\t\t\t'type'\t\t\t\t    : 'iframe',\n" +
                "\t\t\t\t'transitionIn'\t\t\t: 'elastic',\n" +
                "             \t'transitionOut'\t\t\t: 'elastic',\n" +
                "\t\t\t\t onClosed : function(){ \n" +
                "\t\t\t\t\t\t\tvar xmlhttp = getXmlHttp();\n" +
                "\t\t\t\t\t\t\txmlhttp.open('GET', '/include_new/get_basket_sost.php', false);\n" +
                "\t\t\t\t\t\t\txmlhttp.send(null);\n" +
                "\t\t\t\t\t\t\tif(xmlhttp.status == 200) {\n" +
                "\t\t\t\t\t\t\tvar xx = document.getElementById('sosotoyaniekorziny');\n" +
                "\t\t\t\t\t\t\txx.innerHTML=xmlhttp.responseText;\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\tloadprodactions();\n" +
                "\t\t\t\t\t }\n" +
                "\t\t\t});\n" +
                "}\n" +
                "function loaddelivery()\n" +
                "{\n" +
                "\tvar delivstatus=document.getElementById('getfeliv');\n" +
                "\tvar regiontype=document.getElementById('regoine');\n" +
                "\tvar setundev=0;\n" +
                "\tif(delivstatus.checked && regiontype.value!=1)\n" +
                "\tsetundev=1;\n" +
                "\tif(!delivstatus.checked)\n" +
                "\tsetundev=2;\n" +
                "\tvar curbasket=document.getElementById('deliveryspan');\n" +
                "\tvar xmlhttp = getXmlHttp();\n" +
                "\t\t\t\t\txmlhttp.open('GET', '/getdata/get_delivery.php?deltype='+setundev, false);\n" +
                "\t\t\t\t\txmlhttp.send(null);\n" +
                "\t\t\t\t\tif(xmlhttp.status == 200) {\n" +
                "\t\t\t\t\tcurbasket.innerHTML=xmlhttp.responseText;\n" +
                "\t\t\t\t\t}\n" +
                "}\n" +
                "function nodostavka()\n" +
                "{\n" +
                "\tdocument.getElementById('deliveryaddress').style.display='none';\n" +
                "\tdocument.getElementById('delivtr').style.display='none';\n" +
                "\tvar regiontype=document.getElementById('regoine');\n" +
                "\tvar delivsb = document.getElementById('sboplata');\n" +
                "\tif(eval(regiontype.value)==1)  {document.getElementById('beznalopldiv').style.visibility='visible';}\n" +
                "\tif(eval(regiontype.value)==1 && rekvt==1 && delivsb.checked==false)\n" +
                "\t{\n" +
                "\t\tdocument.getElementById('rekvtext').style.display='';\n" +
                "\t    document.getElementById('rekvt').style.display='';\n" +
                "\t}\n" +
                "\taddrt=0;\n" +
                "\tgetsumall();\n" +
                "}\n" +
                "function devfalse()\n" +
                "{\n" +
                "\tdocument.getElementById('deliveryaddress').style.display='none';\n" +
                "\tdocument.getElementById('delivtr').style.display='none';\n" +
                "\tvar regiontype=document.getElementById('regoine');\n" +
                "\tvar delivsb = document.getElementById('sboplata');\n" +
                "\tif(eval(regiontype.value)==1 && rekvt==1  && delivsb.checked==false)\n" +
                "\t{\n" +
                "\t\tdocument.getElementById('rekvtext').style.display='';\n" +
                "\t    document.getElementById('rekvt').style.display='';\n" +
                "\t}\n" +
                "\taddrt=0;\n" +
                "\tvar delivrez = document.getElementById('getsamovyvoz');\n" +
                "\tdelivrez.checked=true;\n" +
                "\tvar delivra = document.getElementById('getfeliv');\n" +
                "\tdelivra.disabled=true;\n" +
                "}\n" +
                "function yesdostavka()\n" +
                "{\t\n" +
                "\n" +
                "\tvar regiontype=document.getElementById('regoine');\n" +
                "\tvar deliv = document.getElementById('delivsumm');\n" +
                "\tdocument.getElementById('beznalopldiv').style.visibility='hidden';\n" +
                "\tif(eval(regiontype.value)==1)\n" +
                "\t\tdocument.getElementById('naloplata').checked=true;\n" +
                "\tgetnal();\n" +
                "\tif(eval(regiontype.value)==1)\n" +
                "\t{\n" +
                "\t\tdeliv.innerHTML=delivprice;\n" +
                "\t\tvar deliv2 = document.getElementById('naloplata');\n" +
                "\t    deliv2.disabled=false;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t{\n" +
                "\t\tdeliv.innerHTML='обговаривается';\n" +
                "\t}\n" +
                "\taddrt=1;\n" +
                "\tdocument.getElementById('deliveryaddress').style.display='';\n" +
                "\tdocument.getElementById('delivtr').style.display='';\n" +
                "\tgetsumall();\n" +
                "}\n" +
                "function plusposition(prodid)\n" +
                "{\n" +
                "\tvar currentcounts = document.getElementById('quant'+prodid);\n" +
                "\tvar currentcount = currentcounts.value;\n" +
                "\tcurrentcounts.value = eval(currentcount)+1;\n" +
                "\tgetsumall();\n" +
                "}\n" +
                "function minusposition(prodid)\n" +
                "{\n" +
                "\tvar currentcounts = document.getElementById('quant'+prodid);\n" +
                "\tvar currentcount = currentcounts.value;\n" +
                "\tif(eval(currentcount)==1)\n" +
                "\t{\n" +
                "\t\tmybasketdel(prodid);\n" +
                "\t}\n" +
                "\telse\n" +
                "\t{\n" +
                "\t\tcurrentcounts.value = eval(currentcount)-1;\n" +
                "\t}\n" +
                "\tgetsumall();\n" +
                "}\n" +
                "function otherdostavka()\n" +
                "{\n" +
                "\tvar deliv = document.getElementById('delivsumm');\n" +
                "\tdeliv.innerHTML='обговаривается';\n" +
                "\t///var deliv = document.getElementById('beznaloplata');\n" +
                "\tvar deliv = document.getElementById('sboplata');\n" +
                "\tdeliv.checked=true;\n" +
                "\tvar deliv2 = document.getElementById('naloplata');\n" +
                "\tdeliv2.disabled=true;\n" +
                "\t/// при запуске закоментить exchange\n" +
                "\t//exchange=1.06;\n" +
                "\taddrt=1;\n" +
                "\t//rekvt=1;\n" +
                "\tgetsumall();\n" +
                "}\n" +
                "function selectdosttype()\n" +
                "{\n" +
                "\tvar regiontype=document.getElementById('regoine');\n" +
                "\tvar dostavkakur=document.getElementById('getfeliv');\n" +
                "\tif(eval(regiontype.value)==1)\n" +
                "\t{\n" +
                "\t\tif(dostavkakur.checked)\n" +
                "\t\t\t{\n" +
                "\t\t\t\tyesdostavka();\n" +
                "\t\t\t}\n" +
                "\t\telse\n" +
                "\t\t\t{\n" +
                "\t\t\t\tnodostavka();\n" +
                "\t\t\t}\n" +
                "\t\t\tvar nalsiv = document.getElementById('nalopldiv');\n" +
                "\t\t\tnalsiv.style.visibility='visible';\n" +
                "\t\t\tvar deliv2 = document.getElementById('naloplata');\n" +
                "\t\t\tdeliv2.disabled=false;\n" +
                "\t\t\tdeliv2.checked=true;\n" +
                "\t\t\tvar sbdivshka = document.getElementById('sbsivopl');\n" +
                "\t\t\tsbdivshka.style.visibility='hidden';\n" +
                "\t\t\tvar sbshka = document.getElementById('sboplata');\n" +
                "\t\t\tsbshka.disabled=true;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t{\n" +
                "\t\tdocument.getElementById('beznalopldiv').style.visibility='hidden';\n" +
                "\t\tif(dostavkakur.checked)\n" +
                "\t\t\t{\n" +
                "\t\t\t\tdocument.getElementById('rekvtext').style.display='none';\n" +
                "\t            document.getElementById('rekvt').style.display='none';\n" +
                "\t\t\t\totherdostavka();\n" +
                "\t\t\t}\n" +
                "\t\telse\n" +
                "\t\t\t{\n" +
                "\t\t\t\t//document.getElementById('rekvtext').style.display='';\n" +
                "\t            //document.getElementById('rekvt').style.display='';\n" +
                "\t\t\t\t////var deliv = document.getElementById('beznaloplata');\n" +
                "\t\t\t\tvar deliv = document.getElementById('sboplata');\n" +
                "\t\t\t\tdeliv.checked=true;\n" +
                "\t\t\t\tvar deliv2 = document.getElementById('naloplata');\n" +
                "\t\t\t\tdeliv2.disabled=true;\n" +
                "\t\t\t\t/// при запуске закоментить exchange\n" +
                "\t\t\t\t//exchange=1.06;\n" +
                "\t\t\t\taddrt=1;\n" +
                "\t\t\t\tnodostavka();\n" +
                "\t\t\t}\n" +
                "\t\t\tvar nalsiv = document.getElementById('nalopldiv');\n" +
                "\t\t\tnalsiv.style.visibility='hidden';\n" +
                "\t\t\tvar sbdivshka = document.getElementById('sbsivopl');\n" +
                "\t\t\tsbdivshka.style.visibility='visible';\n" +
                "\t\t\tvar sbshka = document.getElementById('sboplata');\n" +
                "\t\t\tsbshka.disabled=false;\n" +
                "\t\t\tgetnal();\n" +
                "\t\t\n" +
                "\t}\n" +
                "}\n" +
                "function getnal()\n" +
                "{\n" +
                "\tdocument.getElementById('rekvtext').style.display='none';\n" +
                "\tdocument.getElementById('rekvt').style.display='none';\n" +
                "\tcurexch=1;\n" +
                "\tloadbasket(curexch);\n" +
                "\tgetsumall();\n" +
                "\trekvt=0;\n" +
                "}\n" +
                "\n" +
                "function getbeznal()\n" +
                "{\n" +
                "\t//document.getElementById('rekvtext').style.display='';\n" +
                "\t//document.getElementById('rekvt').style.display='';\n" +
                "\tcurexch=1.07;\n" +
                "\tloadbasket(curexch);\n" +
                "\tgetsumall();\n" +
                "\trekvt=1;\n" +
                "}\n" +
                "function isValidEmailAddress(emailAddress) {\n" +
                "        var pattern = new RegExp(/^((\"[\\w-\\s]+\")|([\\w-]+(?:\\.[\\w-]+)*)|(\"[\\w-\\s]+\")([\\w-]+(?:\\.[\\w-]+)*))(@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$)|(@\\[?((25[0-5]\\.|2[0-4][0-9]\\.|1[0-9]{2}\\.|[0-9]{1,2}\\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\]?$)/i);\n" +
                "        return pattern.test(emailAddress);\n" +
                "    }\n" +
                "function isEmpty(str) {\n" +
                "   for (var i = 0; i < str.length; i++)\n" +
                "      if (\" \" != str.charAt(i))\n" +
                "          return false;\n" +
                "      return true;\n" +
                "}\n" +
                "function setdeliv490price() {\n" +
                "   var allsum=document.getElementById('fullsumm');\n" +
                "   var sumzak = allsum.value;\n" +
                "   if(sumzak>=50000)\n" +
                "   delivprice=0;\n" +
                "   else\n" +
                "   delivprice=490;\n" +
                "   getsumall();\n" +
                "}\n" +
                "function bsformsubmit()\n" +
                "{\n" +
                "\tvar errorinp=0;\n" +
                "\tvar fiot=document.getElementById('fiotext');\n" +
                "\tif(isEmpty(fiot.value))\n" +
                "\t{\n" +
                "\t\tfiot.style.borderColor=\"#FF0000\";\n" +
                "\t\terrorinp=1;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t{\n" +
                "\t\tfiot.style.borderColor=\"#1ca7cc\";\n" +
                "\t}\n" +
                "\tvar emait=document.getElementById('emailtext');\n" +
                "\tif(!isValidEmailAddress(emait.value))\n" +
                "\t{\n" +
                "\t\temait.style.borderColor=\"#FF0000\";\n" +
                "\t\terrorinp=1;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t{\n" +
                "\t\temait.style.borderColor=\"#1ca7cc\";\n" +
                "\t}\n" +
                "\tvar phot=document.getElementById('phonetext');\n" +
                "\tif(isEmpty(phot.value))\n" +
                "\t{\n" +
                "\t\tphot.style.borderColor=\"#FF0000\";\n" +
                "\t\terrorinp=1;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t{\n" +
                "\t\tphot.style.borderColor=\"#1ca7cc\";\n" +
                "\t}\n" +
                "\t/*if(rekvt==1)\n" +
                "\t{\n" +
                "\t\tvar rekt=document.getElementById('rekvtext');\n" +
                "\t\tif(isEmpty(rekt.value))\n" +
                "\t\t{\n" +
                "\t\t\trekt.style.borderColor=\"#FF0000\";\n" +
                "\t\t\terrorinp=1;\n" +
                "\t\t}\n" +
                "\t\telse\n" +
                "\t\t{\n" +
                "\t\t\trekt.style.borderColor=\"#1ca7cc\";\n" +
                "\t\t}\n" +
                "\t}*/\n" +
                "\tif(addrt==1)\n" +
                "\t{\n" +
                "\t\tvar addt=document.getElementById('addresstext');\n" +
                "\t\tif(isEmpty(addt.value))\n" +
                "\t\t{\n" +
                "\t\t\taddt.style.borderColor=\"#FF0000\";\n" +
                "\t\t\terrorinp=1;\n" +
                "\t\t}\n" +
                "\t\telse\n" +
                "\t\t{\n" +
                "\t\t\taddt.style.borderColor=\"#1ca7cc\";\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\tvar errortr=document.getElementById('requiredmess');\n" +
                "\tif(errorinp==0)\n" +
                "\t{\n" +
                "\t\t\n" +
                "\t\terrortr.style.display='none';\n" +
                "\t\tdocument.getElementById('basketform').submit();\n" +
                "\t}\n" +
                "\telse\n" +
                "\t{\n" +
                "\t\terrortr.style.display='';\n" +
                "\t}\n" +
                "}\n" +
                "function parntweformsubmit()\n" +
                "{\n" +
                "\tvar errorinp=0;\n" +
                "\tvar fiot=document.getElementById('fio');\n" +
                "\tif(isEmpty(fiot.value))\n" +
                "\t{\n" +
                "\t\tfiot.style.borderColor=\"#FF0000\";\n" +
                "\t\terrorinp=1;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t{\n" +
                "\t\tfiot.style.borderColor=\"#1ca7cc\";\n" +
                "\t}\n" +
                "\tvar emailt=document.getElementById('email');\n" +
                "\tif(isEmpty(emailt.value))\n" +
                "\t{\n" +
                "\t\temailt.style.borderColor=\"#FF0000\";\n" +
                "\t\terrorinp=1;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t{\n" +
                "\t\temailt.style.borderColor=\"#1ca7cc\";\n" +
                "\t}\n" +
                "\t\n" +
                "\tvar phonet=document.getElementById('phone');\n" +
                "\tif(isEmpty(phonet.value))\n" +
                "\t{\n" +
                "\t\tphonet.style.borderColor=\"#FF0000\";\n" +
                "\t\terrorinp=1;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t{\n" +
                "\t\tphonet.style.borderColor=\"#1ca7cc\";\n" +
                "\t}\n" +
                "\t\n" +
                "\t\tvar icqt=document.getElementById('icq');\n" +
                "\tif(isEmpty(icqt.value))\n" +
                "\t{\n" +
                "\t\ticqt.style.borderColor=\"#FF0000\";\n" +
                "\t\terrorinp=1;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t{\n" +
                "\t\ticqt.style.borderColor=\"#1ca7cc\";\n" +
                "\t}\n" +
                "\t\n" +
                "\t\tvar compnamet=document.getElementById('compname');\n" +
                "\tif(isEmpty(compnamet.value))\n" +
                "\t{\n" +
                "\t\tcompnamet.style.borderColor=\"#FF0000\";\n" +
                "\t\terrorinp=1;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t{\n" +
                "\t\tcompnamet.style.borderColor=\"#1ca7cc\";\n" +
                "\t}\n" +
                "\t\n" +
                "\t\n" +
                "\t\tvar websitet=document.getElementById('website');\n" +
                "\tif(isEmpty(websitet.value))\n" +
                "\t{\n" +
                "\t\twebsitet.style.borderColor=\"#FF0000\";\n" +
                "\t\terrorinp=1;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t{\n" +
                "\t\twebsitet.style.borderColor=\"#1ca7cc\";\n" +
                "\t}\n" +
                "\t\n" +
                "\t\tvar placet=document.getElementById('place');\n" +
                "\tif(isEmpty(placet.value))\n" +
                "\t{\n" +
                "\t\tplacet.style.borderColor=\"#FF0000\";\n" +
                "\t\terrorinp=1;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t{\n" +
                "\t\tplacet.style.borderColor=\"#1ca7cc\";\n" +
                "\t}\n" +
                "\t\n" +
                "\t\n" +
                "\t\n" +
                "\tvar errortr=document.getElementById('requiredmess');\n" +
                "\tif(errorinp==0)\n" +
                "\t{\n" +
                "\t\t\n" +
                "\t\terrortr.style.display='none';\n" +
                "\t\tdocument.getElementById('partnerform').submit();\n" +
                "\t}\n" +
                "\telse\n" +
                "\t{\n" +
                "\t\terrortr.style.display='';\n" +
                "\t}\n" +
                "}\n" +
                "</script>\n" +
                "<body class=\"body_undertop\" topmargin=\"0\" leftmargin=\"0\" bottommargin=\"0\" rightmargin=\"0\" align=\"center\">\n" +
                "<div class=\"success message\"  style=\"top:-150px;\">\n" +
                "  <h3>Сообщение</h3>\n" +
                "  <p>Товар <span id=\"name_added_tov\"></span> успешно добавлен в корзину.</p>\n" +
                "</div>\n" +
                "<table  class=\"table1\" style=\"box-shadow:0px 0px 32px #595959; margin:5px auto; border-raduis: 5px;\" bgcolor=\"#ffffff\"  width=\"1024\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\">\n" +
                "  <tr>\n" +
                "   <td colspan=\"3\" width=\"1024\">\n" +
                "  <table width=\"100%\" border=\"0\" height=\"110px\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin-top: 0px; margin-bottom: 0px;\">\n" +
                "  <tr>\n" +
                "    <td width=\"365px\" rowspan=\"2\" align=\"left\">\n" +
                "\t\t<table width=\"250px\" align=left><tr><td width=\"60px\" height=\"60px\"><img onClick=\"document.location='http://www.playback.ru';return false\" src=\"/img_new/lolo.png\" class=\"logotip\" alt=\"Playback.ru - смартфоны, носимая электроника и аксессуары к ним\" title=\"Playback.ru - смартфоны, носимая электроника и аксессуары к ним\"> </td><td valign=\"center\" align=\"left\"><a class=\"tele_span\" href=\"/\"><span class=\"tele_span_playback\">PlayBack.ru</span></a><br><span style=\"cursor: pointer;\" onClick=\"document.location='/waytoplayback.html';return false\" class=\"getcallback2\">5 минут от метро ВДНХ</span></td></tr>\n" +
                "\t\t</table>\n" +
                "\t</td>\n" +
                "\t<td width=\"3px\" rowspan=\"2\" align=\"center\">&nbsp;\n" +
                "    </td>\n" +
                "    <td width=\"290px\" rowspan=\"2\">\n" +
                "\t\t<table width=\"215px\" align=center><tr><td valign=\"center\" align=\"center\"><span class=\"tele_span\"><nobr><a href=\"tel:+74951437771\">8(495)143-77-71</a></nobr></span><span class=\"grrafik\"><nobr><br>пн-пт: c 11 до 20<br>сб-вс: с 11 до 18</nobr></span></td></tr>\n" +
                "\t\t</table>\n" +
                "    </td>\n" +
                "    <td width=\"3px\"  align=\"center\" rowspan=\"2\">&nbsp;\n" +
                "    </td>\n" +
                "    <td width=\"185px\">\n" +
                "\t\t<table width=\"175px\" align=center><tr><td valign=\"center\" align=\"center\"><span class=\"blocknamezpom\" style=\"cursor: pointer;\" onClick=\"document.location='/tell_about_the_problem.html';return false\" >Возникла проблема?<br>Напишите нам!</span></td></tr>\n" +
                "\t\t</table>\n" +
                "    <span class=\"tele_span\"></span>\n" +
                "   \n" +
                "    </td>\n" +
                "    <td width=\"3px\" align=\"center\">&nbsp;\n" +
                "    </td>\n" +
                "\t<td width=\"179px\">\n" +
                "\t<table  width=\"175px\" align=center><tr><td width=\"53px\" height=\"50px\" rowspan=\"2\" align=\"left\"><a href=\"/basket.html\"><img src=\"/img_new/basket.png\" width=\"49px\" border=0></a></td><td valign=\"bottom\" align=\"left\" height=\"25px\"><a class=\"tele_span2\" href=\"/basket.html\">Корзина</a><br><span class=\"take_me_call\"></span></td></tr>\n" +
                "\t<tr>\n" +
                "\t            <td height=\"10px\" align=\"right\" valign=\"top\"><span class=\"basket_inc_label\" id=\"sosotoyaniekorziny\">пуста</span></td>\n" +
                "\t</tr></table>\n" +
                "\t</td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "    <td colspan=\"3\" style=\"text-align: right;\">\n" +
                "\t<form action=\"/search.php\" method=\"get\" class=\"izkat\">\n" +
                "  <input type=\"search\" name=\"search_string\" placeholder=\"поиск\" class=\"ssstring\"/>\n" +
                "  <input type=\"submit\" name=\"\" value=\"Искать\" class=\"iskat\"/>\n" +
                "</form></td>\n" +
                "   </tr>\n" +
                "\t</table>\n" +
                "\t</td>\n" +
                "  </tr>\n" +
                " </tr>\n" +
                "<!---\t<tr> \n" +
                "\t<td colspan=\"3\" style=\"color: #2556A3; font:17px Roboto-Regular,Helvetica,sans-serif; text-align: center; height: 35px;vertical-align: middle;padding-bottom:10px;\">\n" +
                "\t\t<b>Уважаемые покупатели! C 28 апреля по 8 мая работаем по обычному графику. 9 мая - выходной.</b>\n" +
                "\t</td>\n" +
                "  </tr>--->\n" +
                "  <tr>\n" +
                "    <td colspan=\"3\">\n" +
                "\t\n" +
                "\t\n" +
                "\t\n" +
                "\t\n" +
                "\t<nav>\n" +
                "  <ul class=\"topmenu\">\n" +
                "    <li><a href=\"\" class=\"active\" onclick=\"return false;\"><img src=\"/img/imglist.png\" height=\"9px\"> Каталог<span class=\"fa fa-angle-down\"></span></a>\n" +
                "      <ul class=\"submenu\">\n" +
                "<li><a href=\"/catalog/1652.html\">Чехлы для смартфонов Infinix</a></li><li><a href=\"/catalog/1511.html\">Смартфоны</a></li><li><a href=\"/catalog/1300.html\">Чехлы для смартфонов Xiaomi</a></li><li><a href=\"/catalog/1302.html\">Защитные стекла для смартфонов Xiaomi</a></li><li><a href=\"/catalog/1310.html\">Чехлы для Huawei/Honor</a></li><li><a href=\"/catalog/1308.html\">Чехлы для смартфонов Samsung</a></li><li><a href=\"/catalog/1307.html\">Защитные стекла для смартфонов Samsung</a></li><li><a href=\"/catalog/1141.html\">Планшеты</a></li><li><a href=\"/catalog/1315.html\">Зарядные устройства и кабели</a></li><li><a href=\"/catalog/1329.html\">Держатели для смартфонов</a></li><li><a href=\"/catalog/665.html\">Автодержатели</a></li><li><a href=\"/catalog/1304.html\">Носимая электроника</a></li><li><a href=\"/catalog/1305.html\">Наушники и колонки</a></li><li><a href=\"/catalog/805.html\">Запчасти для телефонов</a></li><li><a href=\"/catalog/1311.html\">Чехлы для планшетов</a></li><li><a href=\"/catalog/1317.html\">Аксессуары для фото-видео</a></li><li><a href=\"/catalog/1318.html\">Чехлы для смартфонов Apple</a></li><li><a href=\"/catalog/1429.html\">USB Флеш-накопители</a></li><li><a href=\"/catalog/1507.html\">Защитные стекла для смартфонов Realme</a></li><li><a href=\"/catalog/1508.html\">Чехлы для смартфонов Realme</a></li><li><a href=\"/catalog/18.html\">Карты памяти</a></li><li><a href=\"/catalog/1303.html\">Защитные стекла для планшетов</a></li><li><a href=\"/catalog/1312.html\">Защитные стекла для смартфонов</a></li><li><a href=\"/catalog/1622.html\">Защитные стекла для смартфонов Apple</a></li><li><a href=\"/catalog/1626.html\">Чехлы для смартфонов Vivo</a></li><li><a href=\"/catalog/1636.html\">Чехлы для смартфонов Tecno</a></li>      </ul>\n" +
                "    </li>\n" +
                "    <li><a href=\"/dostavka.html\">Доставка</a></li>\n" +
                "    <li><a href=\"/pickup.html\">Самовывоз</a></li>\n" +
                "    <li><a href=\"/payment.html\">Оплата</a></li>\n" +
                "    <li><a href=\"/warranty.html\">Гарантия и обмен</a></li>\n" +
                "    <li><a href=\"/contacts.html\">Контакты</a></li>\n" +
                "  </ul>\n" +
                "</nav>\n" +
                "\t\n" +
                "\t\n" +
                "\t\n" +
                "  \n" +
                "  \n" +
                "\t</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td colspan=\"3\" valign=\"top\">\n" +
                "\t<table width=\"100%\" border=\"0\"  cellpadding=\"0\" cellspacing=\"0\">\n" +
                "\t<tr>    <td colspan=\"3\" class=\"item_full_cell\" align=\"left\">\n" +
                "    <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "    <tr>\n" +
                "        <td colspan=\"2\" style=\"text-align:left; padding-left:2px;\">\n" +
                "        <a class=\"button15\" href=\"/\">&#9668 На главную страницу</a></td>\n" +
                "\t\t<div width=\"100%\" height=\"1px\" style=\"display:block;\"></div>\n" +
                "    </tr>\n" +
                "    \n" +
                "      <tr>\n" +
                "        <td colspan=\"2\">\n" +
                "          </td\n" +
                "              ></tr>\n" +
                "            <tr>\n" +
                "              <td class=\"text_cell\">\n" +
                "\t\t\t  <div style=\"width:100%; text-align:center;\"><img src=\"/img/noshop.png\"><br /><span style=\"font-family: Raleway, Helvetica, sans-serif;font-size: 24px;font-weight: bold;color: #104D93;text-decoration: none;\">Ваша корзина пуста.</span><br/><a style=\"font-family: Raleway, Helvetica, sans-serif;font-size: 22px;font-weight: bold;color: #000;text-decoration: underline;\" href=\"/\">Начать покупки</a></div><br/><br/><br/><br/><br/>\n" +
                "\n" +
                "\n" +
                "\n" +
                "\t\t  </td>\n" +
                "  </tr>\n" +
                "    </table></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td colspan=\"3\" align=\"center\">\n" +
                "<div class=\"footer\">\n" +
                "<div class=\"footer_block\">\n" +
                "<span class=\"footer_h1\">Информация</span>\n" +
                "<br>\n" +
                "<a href=\"/\">Наши спецпредложения</a>\n" +
                "<br>\n" +
                "<a href=\"/dostavka.html\">Доставка</a>\n" +
                "<br>\n" +
                "<a href=\"/payment.html\">Оплата</a>\n" +
                "<br>\n" +
                "<a href=\"/warranty.html\">Гарантия</a>\n" +
                "<br>\n" +
                "<a href=\"/contacts.html\">Контакты</a>\n" +
                "<br>\n" +
                "<a href=\"/privacy_policy.html\">Положение о конфиденциальности и защите персональных данных</a>\n" +
                "</div>\n" +
                "<div class=\"footer_block_cont\">\n" +
                "<span class=\"footer_tel\">+7(495)143-77-71</span>\n" +
                "<br><br>\n" +
                "<a class=\"footer_email\" href=\"http://vk.com/playback_ru\"  target=\"_blank\"><img src=\"/img/VK.png\" title=\"Наша страница Вконтакте\"></a>\n" +
                "&nbsp;&nbsp;\n" +
                "<br><br>\n" +
                "\n" +
                "</div>\n" +
                "<div class=\"footer_block_cont\" style=\"width:260px;\">\n" +
                "<span class=\"footer_h1\">График работы:</span>\n" +
                "<br>\n" +
                "пн-пт: c 11-00 до 20-00\n" +
                "<br>\n" +
                "сб-вс: с 11-00 до 18-00\n" +
                "<br><br>\n" +
                "<span class=\"footer_h1\">Наш адрес:</span>\n" +
                "<br>\n" +
                "Москва, Звездный бульвар, 10,\n" +
                "<br>\n" +
                "строение 1, 2 этаж, офис 10.\n" +
                "</div>\n" +
                "<div class=\"footer_block\">\n" +
                "\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"footer_block\">\n" +
                "<script type=\"text/javascript\" src=\"//vk.com/js/api/openapi.js?105\"></script>\n" +
                "<div id=\"vk_groups\"></div>\n" +
                "<script type=\"text/javascript\">\n" +
                "VK.Widgets.Group(\"vk_groups\", {mode: 0, width: \"260\", height: \"210\", color1: 'FFFFFF', color2: '0C5696', color3: '0064BA'}, 48023501);\n" +
                "</script>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"width: 1024px; font-family: Roboto-Regular,Helvetica,sans-serif; text-align: right; font-size: 12px; text-align: left; padding-left: 10px; color: #595959; background: url(/img/footer-fon.png) repeat;\">\n" +
                "2005-2024 &copy;Интернет магазин PlayBack.ru\n" +
                "</div>\n" +
                "<!-- Yandex.Metrika counter -->\n" +
                "<script type=\"text/javascript\" >\n" +
                "   (function(m,e,t,r,i,k,a){m[i]=m[i]||function(){(m[i].a=m[i].a||[]).push(arguments)};\n" +
                "   m[i].l=1*new Date();k=e.createElement(t),a=e.getElementsByTagName(t)[0],k.async=1,k.src=r,a.parentNode.insertBefore(k,a)})\n" +
                "   (window, document, \"script\", \"https://mc.yandex.ru/metrika/tag.js\", \"ym\");\n" +
                "\n" +
                "   ym(232370, \"init\", {\n" +
                "        clickmap:true,\n" +
                "        trackLinks:true,\n" +
                "        accurateTrackBounce:true,\n" +
                "        webvisor:true\n" +
                "   });\n" +
                "</script>\n" +
                "<noscript><div><img src=\"https://mc.yandex.ru/watch/232370\" style=\"position:absolute; left:-9999px;\" alt=\"\" /></div></noscript>\n" +
                "<!-- /Yandex.Metrika counter -->\n" +
                "<!-- BEGIN JIVOSITE CODE {literal} -->\n" +
                "<script type='text/javascript'>\n" +
                "(function(){ var widget_id = '8LKJc6dMce';var d=document;var w=window;function l(){\n" +
                "  var s = document.createElement('script'); s.type = 'text/javascript'; s.async = true;\n" +
                "  s.src = '//code.jivosite.com/script/widget/'+widget_id\n" +
                "    ; var ss = document.getElementsByTagName('script')[0]; ss.parentNode.insertBefore(s, ss);}\n" +
                "  if(d.readyState=='complete'){l();}else{if(w.attachEvent){w.attachEvent('onload',l);}\n" +
                "  else{w.addEventListener('load',l,false);}}})();\n" +
                "</script>\n" +
                "<!-- {/literal} END JIVOSITE CODE -->\n" +
                "</td>\n" +
                "  </tr>\n" +
                "</table>\n" +
                "<a href=\"#\" class=\"scrollup\">Наверх</a>\n" +
                "</body>\n" +
                "</html>";
        content = lemmaFinder.clearPageOfHtmlTags(content);
        Map<String, Integer> result = lemmaFinder.collectLemmas(content);
        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            String word = entry.getKey();
            Integer lemma = entry.getValue();
            System.out.println(word + " - " + lemma);
        }
        System.exit(1);
        Map<String, Integer> result1 = lemmaFinder.collectLemmas("Повторное появление леопарда в Осетии позволяет предположить, что леопард постоянно обитает в некоторых районах Северного Кавказа.");
        for (Map.Entry<String, Integer> entry : result1.entrySet()) {
            String word = entry.getKey();
            Integer lemma = entry.getValue();
            System.out.println(word + " - " + lemma);
        }
    }
}
