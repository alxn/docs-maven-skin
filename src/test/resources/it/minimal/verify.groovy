// This script verifies that a minimal site contains only the barebones of a site.

import com.jcabi.w3c.ValidatorBuilder
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.jsoup.Jsoup

// Verifies that all the files were created
[
    'target/site/index.html'
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

// Verifies the skin info is included
// Footer link
assert html.contains( 'Rendered using <a href="https://github.com/Bernardo-MG/docs-maven-skin">Docs Maven Skin</a>' )
// Note in code
assert html.contains( 'Rendered using Docs Maven Skin' )
assert html.contains( 'Generated by Apache Maven Doxia' )

// Verifies the footer menus were not generated
def titles = body.select( 'dt' )
assert titles.size() == 0

// Verifies that the footer menus are not generated
def navbarFooter = body.select( '#navbar-footer' )
assert navbarFooter.size() == 0

def dlNav = body.select( '.dl-nav' )
assert dlNav.size() == 0

// Verifies metadata is not included
def meta = body.select( 'meta' )
assert meta.size() == 0

// Verifies that the main menus are not generated
def dropdown = body.select( 'li.dropdown' )
assert dropdown.size() == 0
