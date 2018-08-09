// This script verifies that a minimal site contains only the barebones of a site.

import com.jcabi.w3c.ValidatorBuilder
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.jsoup.Jsoup

// Verifies that all the files were created
[
    'target/site/index.html',
    'target/site/favicon.ico',
    'target/site/robots.txt',
    'target/site/css/style.min.css',
    'target/site/js/scripts.min.js'
].each {
    def file = new File(basedir, it)
    if (!file.exists()) {
        throw new IllegalStateException(
            "file ${file} doesn't exist"
        )
    }
}

// Acquires the sample HTML content
def html = new File(basedir, 'target/site/index.html').text

// Validates HTML 5
def htmlResponse = new ValidatorBuilder().html().validate(html)

MatcherAssert.assertThat(
    'There are errors',
    htmlResponse.errors(),
    Matchers.describedAs(htmlResponse.toString(), Matchers.hasSize(0))
)
MatcherAssert.assertThat(
    'There are warnings',
    htmlResponse.warnings(),
    Matchers.describedAs(htmlResponse.toString(), Matchers.hasSize(0))
)

// Parses HTML
def body = Jsoup.parse(html).body()
def head = Jsoup.parse(html).head()

// Verified the heading is set
assert html.contains( '<header class="page-header">' )

// Verifies the title is included in the HTML head
def title = head.select( 'title' )
assert title.html().equals( 'minimal-site – Minimal page' )

// Verifies the title is included in the header
def titleHeader = body.select( '#navbar-main a.navbar-brand' )
assert titleHeader.html().equals( 'minimal-site' )
// Verifies the title includes a relative link to the index
assert titleHeader.attr('href').equals( './index.html' )
// Verifies the project version and date are included
def versionHeader = body.select( '#navbar-main small.navbar-text' )
assert versionHeader.html() =~ /1\.0\.0 \([0-9]+-[0-9]+-[0-9]+\)/

// Footer link
def div = body.select( 'footer.footer div.row div' ).last()
assert div.html().contains( 'Rendered using' )
assert html.contains( 'Rendered using <a href="https://github.com/Bernardo-MG/docs-maven-skin">Docs Maven Skin</a>' )

// Comments before the head
assert html.contains( 'Rendered using Docs Maven Skin' )
assert html.contains( 'Generated by Apache Maven Doxia' )

// Verifies the favicon is included
assert html.contains( '<link href="./favicon.ico" rel="shortcut icon" type="image/x-icon" />' )


// Verifies the footer menus were not generated
def footerTitles = body.select( 'dt' )
assert footerTitles.isEmpty()

// Verifies that the footer navbar is empty
def navbarFooter = body.select( '#navbar-footer' )
assert navbarFooter.isEmpty()

// Verifies that the footer custom content was not generated
def customFooter = body.select( '#footer-custom-content' )
assert customFooter.isEmpty()

def dlNav = body.select( '.dl-nav' )
assert dlNav.isEmpty()

// Verifies that the main menus are not generated
def dropdown = body.select( 'li.dropdown' )
assert dropdown.isEmpty()

// Verifies that the icon menus are not generated
def iconMenu = body.select( 'li > ul.icons-list' )
assert iconMenu.isEmpty()

// Verifies that the right navigation bar was not generated
def rightNavBar = body.select( '.navbar-right' )
assert rightNavBar.isEmpty()

// Verifies the edition link was not created
def edit = head.select( 'a > span.fa-edit' )
assert edit.isEmpty()
